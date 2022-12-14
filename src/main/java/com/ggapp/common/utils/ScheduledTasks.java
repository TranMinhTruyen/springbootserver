package com.ggapp.common.utils;

import com.ggapp.dao.document.DeviceInfo;
import com.ggapp.dao.document.Session;
import com.ggapp.dao.entity.Product;
import com.ggapp.dao.document.ConfirmKey;
import com.ggapp.common.jwt.JWTTokenProvider;
import com.ggapp.dao.repository.mongo.ConfirmKeyRepository;
import com.ggapp.dao.repository.mongo.SessionRepository;
import com.ggapp.dao.repository.mysql.ProductRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

import static com.ggapp.common.utils.Constant.LOGOUT;

@Component
@EnableScheduling
public class ScheduledTasks {

	private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);

	@Autowired
	ConfirmKeyRepository confirmKeyRepository;

	@Autowired
	ProductRepository productRepository;

	@Autowired
	SessionRepository sessionRepository;

	@Autowired
	JWTTokenProvider jwtTokenProvider;

	@Scheduled(fixedDelay = 300000)
	private void clearConfirmKey() {
		LOGGER.info("Start clear confirmkey");
		LocalDateTime now = LocalDateTime.now();
		List<ConfirmKey> getAllConfirmKey = confirmKeyRepository.findAll();
		for (ConfirmKey key: getAllConfirmKey) {
			if (key.getExpire().isBefore(now)){
				confirmKeyRepository.deleteByEmailEquals(key.getEmail());
				LOGGER.info("Confirmkey is deleted: {}", key.getKey());
			}
		}
		getAllConfirmKey.clear();
	}

	@Scheduled(fixedDelay = 300000)
	private void clearSession() {
		LOGGER.info("Start clear session");
		List<Session> sessionList = sessionRepository.findAll();
		for (Session session: sessionList){
			for (DeviceInfo deviceInfo: session.getDeviceInfoList()) {
				if(!jwtTokenProvider.validateToken(deviceInfo.getToken())){
					deviceInfo.setToken(null);
					deviceInfo.setStatus(LOGOUT);
					LOGGER.info("Session is deleted: DeviceName: {}, OS: {}", deviceInfo.getDeviceName(), deviceInfo.getOS());
				}
			}
		}
		sessionRepository.saveAll(sessionList);
		sessionList.clear();
	}

	@Scheduled(cron = "0 0 23 * * ?")
	private void checkProductIsNew() {
		LOGGER.info("Start check product is new");
		List<Product> getAllProduct = productRepository.findAllByNewIsTrue();
		for (Product product: getAllProduct) {
			int checkYear = product.getCreatedDate().getYear();
			Month checkMonth = product.getCreatedDate().getMonth();
			if (LocalDateTime.now().getYear() == checkYear &&
					LocalDateTime.now().getMonth().getValue() - checkMonth.getValue() > 1) {
				productRepository.save(product);
			}
		}
	}
}
