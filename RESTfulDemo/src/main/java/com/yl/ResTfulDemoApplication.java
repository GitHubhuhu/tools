package com.yl;

import com.github.javafaker.Faker;
import com.yl.dao.PieRepository;
import com.yl.entity.Pie;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ResTfulDemoApplication {

	private final Faker faker = new Faker();

	public static void main(String[] args) {
		SpringApplication.run(ResTfulDemoApplication.class, args);
	}


	/**
	 * 初始化数据库
	 * @param repository
	 * @return
     */
	@Bean
	public CommandLineRunner initializeDb(PieRepository repository){
		return (args) -> {
			repository.deleteAll();
			//Insert some random pies
			for(int i = 0; i < 20; i++) {
				repository.save(new Pie(faker.lorem().word(), faker.lorem().sentence()));
			}
		};
	}
}
