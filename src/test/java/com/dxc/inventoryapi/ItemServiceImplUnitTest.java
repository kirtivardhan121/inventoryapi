package com.dxc.inventoryapi;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import com.dxc.inventoryapi.entity.Item;
import com.dxc.inventoryapi.exception.ItemException;
import com.dxc.inventoryapi.repository.ItemRepository;
import com.dxc.inventoryapi.service.ItemService;
import com.dxc.inventoryapi.service.ItemServiceImpl;

@SpringJUnitConfig
public class ItemServiceImplUnitTest {

	@MockBean
	private ItemRepository itemRepository;

	@TestConfiguration
	static class ItemServiceImplTestContextConfiguration {

		@Bean
		public ItemService itemService() {
			return new ItemServiceImpl();
		}
	}

	@Autowired
	private ItemService itemService;

	private Item[] testData;

	@BeforeEach
	public void fillTestData() {

		testData = new Item[] { new Item(101, "RiceOrPaddy", 690, LocalDate.now()),
				new Item(102, "Wheat", 990, LocalDate.now()), new Item(103, "Barley", 850, LocalDate.now()),
				new Item(104, "CocoSeed", 500, LocalDate.now()), new Item(105, "CoffeBean", 900, LocalDate.now()) };
	}

	@AfterEach
	public void clearDatabase() {
		testData = null;
	}

	@Test
	public void addTest() {
		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);
		// Mockito.doNothing().when(itemRepository.save(Mockito.any(Item.class)));

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(false);

		try {
			Item actual = itemService.add(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}

	}

	@Test
	public void addExistingItemTest() {

		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);
		// Mockito.doNothing().when(itemRepository.save(Mockito.any(Item.class)));

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(true);

		Assertions.assertThrows(ItemException.class, () -> {
			itemService.add(testData[0]);
		});

	}

	@Test
	public void UpdateTest() {
		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(true);

		try {
			Item actual = itemService.update(testData[0]);
			Assertions.assertEquals(testData[0], actual);
		} catch (ItemException e) {
			Assertions.fail(e.getMessage());
		}

	}

	@Test
	public void updateNonExistingItemTest() {

		Mockito.when(itemRepository.save(Mockito.any(Item.class))).thenReturn(null);

		Mockito.when(itemRepository.existsById(testData[0].getIcode())).thenReturn(false);

		Assertions.assertThrows(ItemException.class, () -> {
			itemService.update(testData[0]);
		});
	}

	@Test
	public void deleteByIdExistingRecordTest() {
		Mockito.when(itemRepository.existsById(Mockito.isA(Integer.class))).thenReturn(true);

		Mockito.doNothing().when(itemRepository).deleteById(Mockito.isA(Integer.class));

		try {
			Assertions.assertTrue(itemService.deleteById(testData[0].getIcode()));
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void deleteByIdNonExistingRecordTest() {

		Mockito.when(itemRepository.existsById(Mockito.isA(Integer.class))).thenReturn(false);

		Mockito.doNothing().when(itemRepository).deleteById(Mockito.isA(Integer.class));

		Assertions.assertThrows(ItemException.class, () -> {
			itemService.deleteById(testData[0].getIcode());
		});
	}

	@Test
	public void getByIdExistingRecordTest() {
		Mockito.when(itemRepository.findById(testData[0].getIcode())).thenReturn(Optional.of(testData[0]));

		try {
			Assertions.assertEquals(testData[0], itemService.getById(testData[0].getIcode()));
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getByIdNonExistingRecordTest() {
		Mockito.when(itemRepository.findById(testData[0].getIcode())).thenReturn(Optional.empty());

		try {
			Assertions.assertNull(itemService.getById(testData[0].getIcode()));
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}

	}

	@Test
	public void getAllItemsWhenDataExists() {
		List<Item> expected = Arrays.asList(testData);

		Mockito.when(itemRepository.findAll()).thenReturn(expected);

		try {
			Assertions.assertEquals(expected, itemService.getAllItems());
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	@Test
	public void getAllItemsWhenDataNotExists() {
		List<Item> expected = new ArrayList<>();

		Mockito.when(itemRepository.findAll()).thenReturn(expected);

		try {
			Assertions.assertEquals(expected, itemService.getAllItems());
		} catch (ItemException e) {
			Assertions.fail(e.getLocalizedMessage());
		}
	}

	
}
