package com.creative.share.apps.alforat.models;

import java.io.Serializable;
import java.util.List;

public class OfferDataModel implements Serializable {

    private List<Offer> offers;

    public List<Offer> getOffers() {
        return offers;
    }

    public class Offer implements Serializable
    {
        private String offer_id;

        private String offer_image;

        public String getOffer_id() {
            return offer_id;
        }

        public String getOffer_image() {
            return offer_image;
        }
    }
}
