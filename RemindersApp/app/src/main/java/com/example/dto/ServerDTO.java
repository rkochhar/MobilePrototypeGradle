package com.example.dto;

import java.util.List;

public class ServerDTO {
    private String kind;
    private String etag;
    private List<Items> items;

    class Items
    {
        private Key key;
        private String reminderMsg;
        private String dateCreated;
        private int type;

        public Key getKey() {
            return key;
        }

        public void setKey(Key key) {
            this.key = key;
        }

        public String getReminderMsg() {
            return reminderMsg;
        }

        public void setReminderMsg(String reminderMsg) {
            this.reminderMsg = reminderMsg;
        }

        public String getDateCreated() {
            return dateCreated;
        }

        public void setDateCreated(String dateCreated) {
            this.dateCreated = dateCreated;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

    class Key
    {
        private String kind;
        private String appId;
        private String id;
        private boolean complete;

        public String getKind() {
            return kind;
        }

        public void setKind(String kind) {
            this.kind = kind;
        }

        public String getAppId() {
            return appId;
        }

        public String getId() {
            return id;
        }

        public boolean isComplete() {
            return complete;
        }
    }

    public String getKind() {
        return kind;
    }

    public void setKind(String kind) {
        this.kind = kind;
    }

    public String getEtag() {
        return etag;
    }

    public void setEtag(String etag) {
        this.etag = etag;
    }

    public List<Items> getItems() {
        return items;
    }

    public void setItems(List<Items> items) {
        this.items = items;
    }
}
