package com.ca.samples.cloud.reservationclient;

import org.springframework.cloud.stream.annotation.Output;
import org.springframework.messaging.MessageChannel;

public interface ReservationChannels {
	@Output
	MessageChannel output();
}
