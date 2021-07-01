package com.pycogroup.superblog.repository;

import com.pycogroup.superblog.model.User;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE)
public class UserRepositoryTest {
	private static final String TEST_USER_EMAIL = "test@local";
	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private UserRepository userRepository;

	@Before
	public void init() {
		mongoTemplate.remove(User.class).all();
		mongoTemplate.save(User.builder()
			.name(RandomStringUtils.random(40))
			.email("testuser1@local")
			.address("94 Nguyễn Phúc Chu, phường 15, Tân Bình, Hồ Chí Minh")
			.build());
		mongoTemplate.save(User.builder()
			.name(RandomStringUtils.random(40))
			.email("testuser1@local")
			.address("94 Nguyễn Phúc Chu, phường 15, Tân Bình, Hồ Chí Minh")
			.build());

		mongoTemplate.save(User.builder()
			.name(RandomStringUtils.random(40))
			.email("testuser1@local")
			.address("94 Nguyễn Phúc Chu, phường 15, Tân Bình, Hồ Chí Minh")
			.build());
	}

	@Test
	public void testFindByAddressReturnEnoughQuantity() {
		List<User> userList = userRepository.findByAddress("94 Nguyễn Phúc Chu, phường 15, Tân Bình, Hồ Chí Minh");
		Assert.assertEquals(2, userList.size());
	}


}
