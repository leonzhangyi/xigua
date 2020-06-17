package com.water.melon.ui.game.utils;

public class GameBean {

    private boolean success;
    private String error;
    private GameResult result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public GameResult getResult() {
        return result;
    }

    public void setResult(GameResult result) {
        this.result = result;
    }



    public class GameResult {
        private String userName;
        private String accessToken;
        private String expireDateTime;

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAccessToken() {
            return accessToken;
        }

        public void setAccessToken(String accessToken) {
            this.accessToken = accessToken;
        }

        public String getExpireDateTime() {
            return expireDateTime;
        }

        public void setExpireDateTime(String expireDateTime) {
            this.expireDateTime = expireDateTime;
        }

        @Override
        public String toString() {
            return "GameResult{" +
                    "userName='" + userName + '\'' +
                    ", accessToken='" + accessToken + '\'' +
                    ", expireDateTime='" + expireDateTime + '\'' +
                    '}';
        }

        //        	"userName": "kk7510431778",
//                    "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI0IiwibmFtZSI6ImtrNzUxMDQzMTc3OCIsInNzYyI6IlJMN1BFUEJNTVBWU0tRUFlYVExYUVdZVTVWNk43NElJIiwidW5pcXVlX25hbWUiOiJrazc1MTA0MzE3NzgiLCJ0ZWlkIjoidGFsIiwibGx0IjoiMjAyMC0wNS0yNiAyMjo1MTo0NCIsImxsaXAiOiIxMzQuMTc1LjU5LjE0NSIsImV4cCI6MTU5MDYzNDMwNSwiaXNzIjoibW9iaWxldGFsLmNnZGVtb3Rlc3QuY29tIiwiYXVkIjoiYXBwIn0.H2JbdH7j6Axz36TuP-8uSGjC3yykQ-Bu1ypeG6hUFyw",
//                    "expireDateTime": "2020-05-27T22:51:45.128171-04:00"
    }

//    {
//        "success": true,
//            "error": null,
//            "result": {
//        "userName": "kk7510431778",
//                "accessToken": "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJ1aWQiOiI0IiwibmFtZSI6ImtrNzUxMDQzMTc3OCIsInNzYyI6IlJMN1BFUEJNTVBWU0tRUFlYVExYUVdZVTVWNk43NElJIiwidW5pcXVlX25hbWUiOiJrazc1MTA0MzE3NzgiLCJ0ZWlkIjoidGFsIiwibGx0IjoiMjAyMC0wNS0yNiAyMjo1MTo0NCIsImxsaXAiOiIxMzQuMTc1LjU5LjE0NSIsImV4cCI6MTU5MDYzNDMwNSwiaXNzIjoibW9iaWxldGFsLmNnZGVtb3Rlc3QuY29tIiwiYXVkIjoiYXBwIn0.H2JbdH7j6Axz36TuP-8uSGjC3yykQ-Bu1ypeG6hUFyw",
//                "expireDateTime": "2020-05-27T22:51:45.128171-04:00"
//    }
//    }
}
