package com.dxc.inventoryapi.api;

import java.time.LocalDate;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;
import com.dxc.inventoryapi.service.ItemService;

@RestController
@RequestMapping("/items")
public class ItemApi {

	@Autowired
	private ItemService itemService;

	@GetMapping
	public ResponseEntity<List<Item>> getAllItems() throws ItemException {

		return new ResponseEntity<>(itemService.getAllItems(), HttpStatus.OK);
	}

	@GetMapping("/{itemId:[0-9]{1,5}}")
	public ResponseEntity<Item> getItemsById(@PathVariable("itemId") int icode) throws ItemException {

		ResponseEntity<Item> response = null;

		Item item = itemService.getById(icode);

		if (item == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(item, HttpStatus.OK);
		}
		return response;
	}

	@PostMapping
	public ResponseEntity<Item> createItem(@Valid @RequestBody Item item, BindingResult result) throws ItemException {

		ResponseEntity<Item> response = null;

		if (result.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new ItemException(errMsg.toString());
		} else {
			itemService.add(item);
			response = new ResponseEntity<>(item, HttpStatus.OK);
		}
		return response;
	}

	@PutMapping
	public ResponseEntity<Item> updateItem(@Valid @RequestBody Item item, BindingResult result) throws ItemException {

		ResponseEntity<Item> response = null;

		if (result.hasErrors()) {
			StringBuilder errMsg = new StringBuilder();
			for (FieldError err : result.getFieldErrors()) {
				errMsg.append(err.getDefaultMessage() + ",");
			}
			throw new ItemException(errMsg.toString());
		} else {
			itemService.update(item);
			response = new ResponseEntity<>(item, HttpStatus.OK);
		}
		return response;
	}

	@DeleteMapping("/{itemId}")
	public ResponseEntity<Void> deleteItemsById(@PathVariable("itemId") int icode) throws ItemException {

		itemService.deleteById(icode);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@GetMapping("/{iname:[A-Za-z]{4,20}}")
	public ResponseEntity<Item> getItemByIname(@PathVariable("iname") String iname) throws ItemException {

		ResponseEntity<Item> response = null;

		Item item = itemService.findByIname(iname);

		if (item == null) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(item, HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/{packageDate:[0-9]{4}-[0-9]{2}-[0-9]{2}}")
	public ResponseEntity<List<Item>> getItemByPackageDate(
			@PathVariable("packageDate") @DateTimeFormat(iso = ISO.DATE) LocalDate packageDate) throws ItemException {

		ResponseEntity<List<Item>> response = null;

		List<Item> items = itemService.findByPackageDate(packageDate);

		if (items == null || items.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(items, HttpStatus.OK);
		}
		return response;
	}

	@GetMapping("/priceBetween/{lbound}/and/{ubound}")
	public ResponseEntity<List<Item>> getItemByPriceRange(@PathVariable("lbound") double lbound,
			@PathVariable("ubound") double ubound) throws ItemException {

		ResponseEntity<List<Item>> response = null;

		List<Item> items = itemService.findByPriceRange(lbound, ubound);

		if (items == null || items.size() == 0) {
			response = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			response = new ResponseEntity<>(items, HttpStatus.OK);
		}
		return response;
	}
}
