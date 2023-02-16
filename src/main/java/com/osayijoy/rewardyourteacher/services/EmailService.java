package com.osayijoy.rewardyourteacher.services;

import com.osayijoy.rewardyourteacher.dto.EmailResponse;
import com.osayijoy.rewardyourteacher.utils.EmailDetails;

public interface EmailService {

    EmailResponse sendSimpleMail(EmailDetails details, String email);
    EmailResponse sendMailWithAttachment(EmailDetails details, String email);
}
