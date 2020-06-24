package com.dxc.inventoryapi.entity;

import java.io.Serializable;
import java.time.LocalDate;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

@Entity
@Table(name = "items")
public class Item implements Serializable {

	@Id
	@Column(name = "icode")
	@NotNull(message = "icode can not be blank")
	private Integer icode;

	@Column(name = "iname", nullable = false)
	@NotBlank(message = "iname can not be blank")
	@Size(min = 4, max = 20, message = "Name must be of 4 to 20 characters in length")
	private String iname;

	@Column(name = "price", nullable = false)
	@NotNull(message = "price can not be blank")
	@Min(value = 20, message = "minimum price is expected to be 20")
	@Max(value = 1000, message = "maximum price is expected not more than 1000")
	private double price;

	@Column(name = "pdate", nullable = true)
	@NotNull(message = "packageDate can not be blank")
	@PastOrPresent(message = "Package date can not be a future date")
	@DateTimeFormat(iso = ISO.DATE)
	private LocalDate packageDate;

	public Item() {
		// left unimplemented
	}

	public Item(int icode, String iname, double price, LocalDate packageDate) {
		super();
		this.icode = icode;
		this.iname = iname;
		this.price = price;
		this.packageDate = packageDate;
	}

	public Integer getIcode() {
		return icode;
	}

	public void setIcode(Integer icode) {
		this.icode = icode;
	}

	public String getIname() {
		return iname;
	}

	public void setIname(String iname) {
		this.iname = iname;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public LocalDate getPackageDate() {
		return packageDate;
	}

	public void setPackageDate(LocalDate packageDate) {
		this.packageDate = packageDate;
	}

}