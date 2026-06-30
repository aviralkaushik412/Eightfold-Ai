package com.aviral.normalizer;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

public class PhoneNormalizer {
    private static final PhoneNumberUtil PHONE_NUMBER_UTIL = PhoneNumberUtil.getInstance();

    public String normalize(String phoneNumber) {
        if (phoneNumber == null || phoneNumber.isBlank()) {
            return null;
        }

        try {
            Phonenumber.PhoneNumber parsedNumber = PHONE_NUMBER_UTIL.parse(phoneNumber, "IN");
            if (!PHONE_NUMBER_UTIL.isValidNumber(parsedNumber)) {
                return null;
            }
            return PHONE_NUMBER_UTIL.format(parsedNumber, PhoneNumberUtil.PhoneNumberFormat.E164);
        } catch (NumberParseException e) {
            return null;
        }
    }
}
