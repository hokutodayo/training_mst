package com.example.demo.entity;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Entity
@Data
@Table(name = "users")
public class User {

	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "user_name")
	@NotBlank(message = "名前を入力してください")
	@Size(max = 50, message = "名前は50文字以内で入力してください")
	private String name;

	@Column(name = "address")
	@Size(max = 100, message = "住所は100文字以内で入力してください")
	private String address;

	@Column(name = "phone")
	@Pattern(regexp = "|\\d{1,4}-\\d{1,4}-\\d{4}", message = "電話番号の形式（xxxx-xxxx-xxxx）で入力してください")
	private String phone;

	@Column(name = "update_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime updateDate;

	@Column(name = "create_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime createDate;

	@Column(name = "delete_date")
	@DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
	private LocalDateTime deleteDate;

}
