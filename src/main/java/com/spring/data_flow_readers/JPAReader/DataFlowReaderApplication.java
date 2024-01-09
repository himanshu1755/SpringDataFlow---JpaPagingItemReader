package com.spring.data_flow_readers.JPAReader;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.task.configuration.EnableTask;

@SpringBootApplication
@EnableTask
public class DataFlowReaderApplication {

	public static void main(String[] args) {
		SpringApplication.run(DataFlowReaderApplication.class, args);
	}

}
