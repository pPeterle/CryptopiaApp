package com.example.pedro.myapplication.data.remote.client;

import com.example.pedro.myapplication.data.remote.constants.ApiConstants;
import com.example.pedro.myapplication.data.remote.exceptions.CryptopiaException;
import com.google.gson.JsonObject;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class CryptopiaClientImpl {

    private String apiKey;

    private String secretKey;

    public CryptopiaClientImpl(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }
/*
    *//**
     * 连通性测试，因为cryptopia不提供该api，因此我们执行一次btc的余额查询。
     *//*
    @Override
    public void ping() {

        JsonObject params = new JsonObject();
        params.addProperty("Currency", "BTC");

        try {
            Response response = request("GetBalance", params.toString());
            assert response.body().string().length() >= 0;
        } catch (Exception ex) {
            throw new CryptopiaException("[ping](getbalance of btc in fact) failed", ex);
        }
    }

    @Override
    public Market getMarket(final String symbol) {

        try {
            final String action = String.format("GetMarket/%s", symbol);
            Response response = simpleRequest(action);
            return Market.Companion.parse(response.body().string());
        } catch (IOException ex) {
            throw new CryptopiaException("failed read response while getMarket.", ex);
        }

    }

    @Override
    public List<Balance> getAllBalances() {
        String currency = null;
        try {
            JsonObject params = new JsonObject();
            params.addProperty("Currency", currency);

            Response response = request("GetBalance", params.toString());
            return Balance.Companion.parse(response.body().string());
        } catch (IOException ex) {
            throw new CryptopiaException("failed read response while GetBalance.", ex);
        }
    }

    @Override
    public Balance getBalance(String currency) {

        try {
            JsonObject params = new JsonObject();
            params.addProperty("Currency", currency);

            Response response = request("GetBalance", params.toString());

            List<Balance> balances = Balance.Companion.parse(response.body().string());
            if (!balances.isEmpty()) {
                return balances.get(0);
            }
        } catch (IOException ex) {
            throw new CryptopiaException("failed read response while GetBalance.", ex);
        }

        return null;
    }

    @Override
    public Trade submitTrade(TradeRequest request) {

        final String action = "SubmitTrade";

        final JsonObject params = new JsonObject();
        if (request.getMarket() != null) {
            params.addProperty("Market", request.getMarket());
        }
        if ((request.getMarket() == null || request.getMarket().length() == 0)
                && request.getTradePairId() != null) {
            params.addProperty("TradePairId", request.getTradePairId());
        }
        params.addProperty("Type", request.getType().getLabel());
        params.addProperty("Rate", request.getRate());
        params.addProperty("Amount", request.getAmount());

        try {
            Response response = request(action, params.toString());
            return Trade.Companion.parse(response.body().string());
        } catch (IOException ex) {
            throw new CryptopiaException("[submitTrade] failed.", ex);
        }
    }

    @Override
    public List<TradeDetail> getTradeHistory(String symbol, String count) {
        final String action = "GetTradeHistory";
        final JsonObject params = new JsonObject();
        params.addProperty("Market", symbol);
        params.addProperty("Count", count);

        try {
            Response response = request(action, params.toString());

            return TradeDetail.Companion.parse(response.body().string());
        } catch (IOException ex) {
            throw new CryptopiaException("[GetTradeHistory] failed", ex);
        }
    }


    *//**
     * for private api
     *
     * @return http response instance
     *//*
    private Response request(String action, String paramStr) throws IOException {

        final String uri = String.format("%s%s", ApiConstants.API_BASE_URL, action);

        Request request = new Request.Builder()
                .headers(buildHeaders(uri, paramStr))
                .url(uri)
                .post(RequestBody.create(MediaType.parse(ApiConstants.CONTENT_TYPE_APPLICATION_JSON), paramStr))
                .build();

        return httpClient.newCall(request).execute();
    }

    *//**
     * for public api
     *
     * @return http response instance
     *//*
    private Response simpleRequest(final String action) throws IOException {

        final String uri = String.format("%s%s", ApiConstants.API_BASE_URL, action);
        Request request = new Request.Builder()
                // .addHeader(ApiConstants.HEADER_CONTENT_TYPE, ApiConstants.CONTENT_TYPE_APPLICATION_JSON)
                .url(uri)
                .build();

        return httpClient.newCall(request).execute();
    }

    private Headers buildHeaders(final String uri, String paramStr) {

        return new Headers.Builder()
                // .add(ApiConstants.HEADER_CONTENT_TYPE, ApiConstants.CONTENT_TYPE_APPLICATION_JSON)
                .add(ApiConstants.HEADER_AUTHORIZATION, getAuthString(uri, paramStr))
                .build();
    }*/

    public String getAuthString(String uri, JsonObject postParam) {
        try {
            final String nonce = String.valueOf(System.currentTimeMillis());

            final StringBuilder requestSignature = new StringBuilder();
            requestSignature.append(this.apiKey)
                    .append("POST")
                    .append(URLEncoder.encode(ApiConstants.API_BASE_URL + uri, StandardCharsets.UTF_8.toString()).toLowerCase())
                    .append(nonce)
                    .append(getMd5B64String(postParam.toString()));

            final StringBuilder auth = new StringBuilder();
            auth.append(ApiConstants.AUTHENTICATION_SCHEMA)
                    .append(this.apiKey)
                    .append(":")
                    .append(getHmacSha256B64String(requestSignature.toString()))
                    .append(":")
                    .append(nonce);

            return auth.toString();

        } catch (UnsupportedEncodingException e) {
            throw new CryptopiaException("Unsupport encoding exception.", e);
        } catch (NoSuchAlgorithmException e) {
            throw new CryptopiaException("no such algorithm exception.", e);
        } catch (InvalidKeyException e) {
            throw new CryptopiaException("invalid key exception.", e);
        }
    }

    private String getMd5B64String(String postParameter)
            throws NoSuchAlgorithmException, UnsupportedEncodingException {

        MessageDigest md5Digest = MessageDigest.getInstance(ApiConstants.HASH_ALGORITHM_MD5);
        byte[] digestBytes = md5Digest.digest(postParameter.getBytes(ApiConstants.UTF_8));
        return android.util.Base64.encodeToString(digestBytes, android.util.Base64.DEFAULT).trim();
    }

    private String getHmacSha256B64String(String msg)
            throws NoSuchAlgorithmException, InvalidKeyException, UnsupportedEncodingException {

        Mac hmacSHA2561 = Mac.getInstance("HmacSHA256");
        SecretKeySpec secretSpec1 = new SecretKeySpec(
                android.util.Base64.decode(this.secretKey, android.util.Base64.DEFAULT), ApiConstants.SIGN_ALGORITHM_HMAC_SHA256);

        hmacSHA2561.init(secretSpec1);
        String android64 = android.util.Base64.encodeToString(hmacSHA2561.doFinal(msg.getBytes(ApiConstants.UTF_8)), android.util.Base64.DEFAULT);

        return android64.trim();
    }
}
