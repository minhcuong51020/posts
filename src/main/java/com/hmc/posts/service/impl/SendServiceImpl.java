package com.hmc.posts.service.impl;

import com.hmc.client.SocialClient;
import com.hmc.common.dto.response.LineDTO;
import com.hmc.common.dto.response.RedditDTO;
import com.hmc.common.dto.response.TwitterDTO;
import com.hmc.common.enums.error.AuthenticationError;
import com.hmc.common.exception.ResponseException;
import com.hmc.common.util.IdUtils;
import com.hmc.config.SecurityUtils;
import com.hmc.posts.dto.request.PostLineRequest;
import com.hmc.posts.dto.request.PostRedditCreateRequest;
import com.hmc.posts.dto.request.PostTwitterRequest;
import com.hmc.posts.dto.request.PostUserInfoRequest;
import com.hmc.posts.entity.*;
import com.hmc.posts.repository.*;
import com.hmc.posts.service.SendService;
import com.hmc.posts.support.BadRequestError;
import com.hmc.posts.support.ConvertText;
import com.hmc.posts.support.enums.TypePostUserInfo;
import com.linecorp.bot.client.ChannelTokenSupplier;
import com.linecorp.bot.client.FixedChannelTokenSupplier;
import com.linecorp.bot.client.LineMessagingClient;
import com.linecorp.bot.model.Broadcast;
import com.linecorp.bot.model.message.TextMessage;
import com.linecorp.bot.model.request.SetWebhookEndpointRequest;
import com.twilio.Twilio;
import com.twilio.type.PhoneNumber;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;
import org.apache.http.util.EntityUtils;
import org.springframework.transaction.annotation.Transactional;
import org.w3c.dom.Text;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.v1.Status;
import twitter4j.v1.StatusUpdate;
import twitter4j.v1.UploadedMedia;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.time.LocalDate;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class SendServiceImpl implements SendService {

    private final PostRedditRepository postRedditRepository;

    private final PostRepository postRepository;

    private final UserInfoRepository userInfoRepository;

    private final EmailRepository emailRepository;

    private final PostUserInfoRepository postUserInfoRepository;

    private final PostLineRepository postLineRepository;

    private final PostTwitterRepository postTwitterRepository;

    private final SocialClient socialClient;

    private final ConvertText convertText;

    @Value("${sms.account_sid}")
    private String ACCOUNT_SID;

    @Value("${sms.auth_token}")
    private String AUTH_TOKEN;

    @Value("${sms.from_phone}")
    private String fromPhone;

    @Override
    public void postToEmail(PostUserInfoRequest request) {
        String ownerId = ensureUserIdLogin();
        EmailEntity email = ensureEmailEntity(request.getEmailId());
        String postId = request.getPostId();
        PostEntity postEntity = ensurePostEntity(postId);
        List<UserInfoEntity> userInfoEntities =
                this.userInfoRepository.findAllByIds(request.getUserInfoIds());
        Map<String, String> userEmails = new HashMap<>();
        userInfoEntities.forEach((item) -> {
            userEmails.put(item.getId(), item.getEmail());
        });
        String title = postEntity.getTitle();
        String content = postEntity.getContent();
        sendToEmail(title, content, email.getEmail(), email.getPassword(), userEmails);
        List<PostUserInfoEntity> postUserInfoEntities = new ArrayList<>();
        userEmails.forEach((key, value) -> {
            PostUserInfoEntity postUserInfoEntity = new PostUserInfoEntity();
            postUserInfoEntity.setId(IdUtils.nextId());
            postUserInfoEntity.setPostId(postId);
            postUserInfoEntity.setOwnerId(ownerId);
            postUserInfoEntity.setUserInfoId(key);
            postUserInfoEntity.setType(TypePostUserInfo.EMAIL);
            postUserInfoEntity.setTimeSend(LocalDate.now());
            postUserInfoEntities.add(postUserInfoEntity);
        });
        this.postUserInfoRepository.saveAll(postUserInfoEntities);
    }

    @Override
    public void postToSms(PostUserInfoRequest request) {
        PostEntity postEntity = ensurePostEntity(request.getPostId());
        List<UserInfoEntity> userInfoEntities = this.userInfoRepository.findAllByIds(request.getUserInfoIds());
        Map<String, String> userPhones = new HashMap<>();
        userInfoEntities.stream().forEach((item) -> {
            userPhones.put(item.getId(), item.getPhone());
        });
        String title = postEntity.getTitle();
        String content = postEntity.getContent();
        Map<String, String> userPhoneSuccesses = new HashMap<>();
        sendToSms(title, content, userPhones, userPhoneSuccesses);
        List<PostUserInfoEntity> postUserInfoEntities = new ArrayList<>();
        userPhoneSuccesses.forEach((key, value) -> {
            PostUserInfoEntity postUserInfoEntity = new PostUserInfoEntity();
            postUserInfoEntity.setId(IdUtils.nextId());
            postUserInfoEntity.setPostId(request.getPostId());
            postUserInfoEntity.setOwnerId(ensureUserIdLogin());
            postUserInfoEntity.setUserInfoId(key);
            postUserInfoEntity.setType(TypePostUserInfo.SMS);
            postUserInfoEntity.setTimeSend(LocalDate.now());
            postUserInfoEntities.add(postUserInfoEntity);
            postUserInfoEntities.add(postUserInfoEntity);
        });
        this.postUserInfoRepository.saveAll(postUserInfoEntities);
    }

    @Override
    public boolean postToReddit(PostRedditCreateRequest request) {
        String ownerId = ensureUserIdLogin();
        String postId = request.getPostId();
        boolean sendSuccess = false;
        RedditDTO redditDTO = socialClient.getRedditById(request.getRedditId()).getData();
        if(StringUtils.isBlank(redditDTO.getId())) {
            throw new ResponseException(BadRequestError.USER_INFO_NOT_FOUND);
        }
        List<PostRedditEntity> postRedditEntities = new ArrayList<PostRedditEntity>();
        for(String redditId : request.getRedditIds()) {
            PostRedditEntity postRedditEntity = new PostRedditEntity();
            postRedditEntity.setId(IdUtils.nextId());
            postRedditEntity.setPostId(postId);
            postRedditEntity.setRedditGroupId(redditId);
            postRedditEntity.setOwnerId(ownerId);
            postRedditEntity.setCreatedAt(LocalDate.now());
            postRedditEntities.add(postRedditEntity);
        }
        String tokenReddit = "";
        try {
            tokenReddit = getTokenReddit(redditDTO.getUsername(), redditDTO.getPassword(),
                    redditDTO.getClientId(), redditDTO.getClientSecret());
        } catch (Exception e) {
            throw new ResponseException(BadRequestError.USER_INFO_NOT_FOUND);
        }
        if(!StringUtils.isBlank(tokenReddit)) {
            try {
                PostEntity postEntity = this.postRepository.findById(postId).orElseThrow(
                        () -> new ResponseException(BadRequestError.POST_NOT_FOUND)
                );
                String title = postEntity.getTitle();
                String content = this.convertText.convertHtmlToText(postEntity.getContent());
                sendToReddit(tokenReddit, title, content, request.getRedditNameUrls());
            } catch (Exception e) {
                log.info("Token bad");
            }
            this.postRedditRepository.saveAll(postRedditEntities);
            sendSuccess = true;
        }
        return sendSuccess;
    }

    @Override
    public void postToLine(PostLineRequest request) {
        PostEntity postEntity = ensurePostEntity(request.getPostId());
        Document doc = Jsoup.parse(postEntity.getContent());
        Elements elements = doc.getElementsByTag("img");
        List<String> imageUrls = new ArrayList<>();
        elements.forEach((element) -> {
            imageUrls.add(element.attr("src"));
        });
        String title = postEntity.getTitle();
        String content = Jsoup.parse(postEntity.getContent()).wholeText();
        LineDTO lineDTO = socialClient.getLineById(request.getLineId()).getData();
        if(!Objects.nonNull(lineDTO) || StringUtils.isBlank(lineDTO.getChannelToken())) {
            throw new ResponseException(BadRequestError.LINE_NOT_FOUND);
        }
        sendToLine(title, content, imageUrls, lineDTO.getChannelToken());
        PostLineEntity postLineEntity = new PostLineEntity();
        postLineEntity.setId(IdUtils.nextId());
        postLineEntity.setLineId(request.getLineId());
        postLineEntity.setPostId(request.getPostId());
        postLineEntity.setOwnerId(ensureUserIdLogin());
        postLineEntity.setTimeSend(LocalDate.now());
        this.postLineRepository.save(postLineEntity);
    }

    @Override
    public void postToTwitter(PostTwitterRequest request) {
        PostEntity postEntity = ensurePostEntity(request.getPostId());
        Document doc = Jsoup.parse(postEntity.getContent());
        Elements elements = doc.getElementsByTag("img");
        List<String> imageUrls = new ArrayList<>();
        elements.forEach((element) -> {
            imageUrls.add(element.attr("src"));
        });
        String title = postEntity.getTitle();
        String content = Jsoup.parse(postEntity.getContent()).wholeText();
        TwitterDTO twitterDTO = socialClient.getTwitterById(request.getTwitterId()).getData();
        String contentLast = title + "\n" + content;
        try {
            sendToTwitter(content, imageUrls, twitterDTO);
        } catch (Exception e) {
            throw new ResponseException(BadRequestError.SEND_TWITTER_ERROR);
        }
        PostTwitterEntity postTwitter = new PostTwitterEntity();
        postTwitter.setId(IdUtils.nextId());
        postTwitter.setPostId(request.getPostId());
        postTwitter.setTwitterId(request.getTwitterId());
        postTwitter.setOwnerId(ensureUserIdLogin());
        postTwitter.setTimeSend(LocalDate.now());
        this.postTwitterRepository.save(postTwitter);
    }

    private String ensureUserIdLogin() {
        return SecurityUtils.getCurrentUserLoginId().orElseThrow(
                () -> new ResponseException(AuthenticationError.AUTHENTICATION_ERROR)
        );
    }

    private PostEntity ensurePostEntity(String postId) {
        return this.postRepository.findById(postId).orElseThrow(
                () -> new ResponseException(BadRequestError.POST_NOT_FOUND)
        );
    }

    private EmailEntity ensureEmailEntity(String id) {
        return this.emailRepository.findById(id).orElseThrow(
                () -> new ResponseException(BadRequestError.EMAIL_NOT_FOUND)
        );
    }

    private String getTokenReddit(String username, String password, String clientId, String clientSecret) {
        final String url = "https://www.reddit.com/api/v1/access_token";
        CredentialsProvider credentialsProvider = new BasicCredentialsProvider();
        credentialsProvider.setCredentials(
                AuthScope.ANY,
                new UsernamePasswordCredentials(clientId, clientSecret)
        );
        HttpClient httpClient = HttpClientBuilder.create()
                .setDefaultCredentialsProvider(credentialsProvider)
                .build();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>(3);
        params.add(new BasicNameValuePair("grant_type", "password"));
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("password", password));
        String responseString = "";
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            httpPost.setHeader("User-Agent", "u/MinhCuong2000");
            HttpResponse response = httpClient.execute(httpPost);
            responseString = EntityUtils.toString(response.getEntity(), HTTP.UTF_8);
            String token = responseString.substring(18, 62);
            return token;
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        } catch (ClientProtocolException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private void sendToReddit(String token, String title, String content, List<String> groups) throws IOException, InterruptedException {
        System.out.println(content);
        final String url = "https://oauth.reddit.com/api/submit";
        CloseableHttpClient httpClient = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("api_type", "json"));
        params.add(new BasicNameValuePair("kind", "self"));
        params.add(new BasicNameValuePair("title", title));
        params.add(new BasicNameValuePair("text", content));
        httpPost.setHeader("Accept", "*/*");
        httpPost.setHeader("Content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("Authorization", "bearer " + token);
        httpPost.setHeader("User-Agent", "u/MinhCuong2000");
        for (String group : groups) {
            params.add(new BasicNameValuePair("sr", group));
            httpPost.setEntity(new UrlEncodedFormEntity(params, StandardCharsets.UTF_8));
            HttpResponse response = httpClient.execute(httpPost);
            System.out.println(EntityUtils.toString(response.getEntity(), HTTP.UTF_8));
        }
    }

    private void sendToEmail(String title, String content, String email,
                               String password, Map<String, String> userEmails) {
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS

        Session session = Session.getInstance(prop,
                new Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(email, password);
                    }
                });
        try {
            StringBuilder toEmails = new StringBuilder();
            userEmails.forEach( (key, value) -> {
                toEmails.append(value);
                toEmails.append(",");
            });
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(email));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmails.toString())
            );
            message.setSubject(title);
            message.setContent(content, "text/html; charset=utf-8");
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Gửi email thất bại");
            throw new ResponseException(BadRequestError.SEND_EMAIL_ERROR);
        }
    }

    private void sendToSms(String title, String content,
                           Map<String, String> toPhones, Map<String, String> toPhoneSuccesses) {
        Twilio.init(ACCOUNT_SID, AUTH_TOKEN);
        final String prefix = "+84";
        toPhones.forEach((key, value) -> {
            try {
                String toPhone = prefix + value;
                String sms = title + "\n" + convertText.convertHtmlToText(content);
                com.twilio.rest.api.v2010.account.Message.creator(new PhoneNumber(toPhone),
                        new PhoneNumber(fromPhone),
                        sms).create();
                toPhoneSuccesses.put(key, value);
            } catch (Exception e) {
                log.error("Gửi sms thất bại");
            }
        });
    }

    private void sendToLine(String title, String content, List<String> imageUrls, String channelToken) {
        ChannelTokenSupplier channelTokenSupplier = FixedChannelTokenSupplier.of(channelToken);
        LineMessagingClient client =  LineMessagingClient.builder(channelTokenSupplier)
                .build();
        client.setWebhookEndpoint(SetWebhookEndpointRequest.builder()
                .endpoint(URI.create("/callback"))
                .build());
        String text = title + "\n" + content;
        com.linecorp.bot.model.message.Message textMessage = new TextMessage(text);
        Broadcast broadcastText = new Broadcast(textMessage);
        client.broadcast(broadcastText);
        imageUrls.forEach((item) -> {
            com.linecorp.bot.model.message.Message imgMessage =
                    com.linecorp.bot.model.message.ImageMessage.builder()
                            .originalContentUrl(URI.create(item))
                            .previewImageUrl(URI.create(item))
                            .build();
            Broadcast broadcastImage = new Broadcast(imgMessage);
            System.out.println(item);
            client.broadcast(broadcastImage);
        });
    }

    private void sendToTwitter(String content, List<String> imageUrls, TwitterDTO twitterDTO) throws TwitterException {
        String consumerKey = twitterDTO.getConsumerKey();
        String consumerSecret = twitterDTO.getConsumerSecret();
        String oauthToken = twitterDTO.getOauthToken();
        String oauthTokenSecret = twitterDTO.getOauthTokenSecret();
        Twitter twitter = Twitter.newBuilder()
                .oAuthConsumer(consumerKey, consumerSecret)
                .oAuthAccessToken(oauthToken, oauthTokenSecret)
                .build();
        List<Long> mediaList = new ArrayList<>();
        if(Objects.nonNull(imageUrls)) {
            imageUrls.forEach((item) -> {
                URL url = null;
                try {
                    url = new URL(item);
                    InputStream in = url.openStream();
                    UploadedMedia media = twitter.v1().tweets().uploadMedia("1", in);
                    mediaList.add(media.getMediaId());
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (TwitterException e) {
                    throw new RuntimeException(e);
                }
            });
        }
        long[] mediaIds = new long[mediaList.size()];
        for (int i = 0; i < mediaList.size(); i++) {
            mediaIds[i] = mediaList.get(i);
        }
        StatusUpdate statusUpdate = StatusUpdate.of(content);
        statusUpdate = statusUpdate.mediaIds(mediaIds);
        Status status = twitter.v1().tweets().updateStatus(statusUpdate);
    }

}
