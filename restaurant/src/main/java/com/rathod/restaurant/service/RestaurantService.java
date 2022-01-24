package com.rathod.restaurant.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.rathod.restaurant.dto.RefillIAndAcceptItemDto;
import com.rathod.restaurant.entity.Item;
import com.rathod.restaurant.entity.Restaurant;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class RestaurantService {
	
	private final RecordInitializer recordInitializer;
	private List<Restaurant> restaurants;
	
	public void reInitialize() throws Exception {
		restaurants = recordInitializer.readInitializerFile();
	}

	public boolean refillItem(RefillIAndAcceptItemDto refill) {
		
		Boolean itemExistFlag = false;
		for(Restaurant restaurant: restaurants)
		{
			if(restaurant.getRestaurantId().equals(refill.getRestId()))
			{
				for(Item item: restaurant.getItems())
				{
					if(item.getItemId().equals(refill.getItemId()))
					{
						log.info("Item before refill :" + item.getItemId()+" ||  "+item.getQuantity());
						item.setQuantity(item.getQuantity() + refill.getQty());
						log.info("Item after refill :" + item.getItemId()+" ||  "+item.getQuantity());
						itemExistFlag = true;
					}
				}
			}
		}
		
		return itemExistFlag;
	}

	public boolean acceptOrder(RefillIAndAcceptItemDto accept) {
		Boolean sufficientQuantityFlag = false;
		for(Restaurant restaurant: restaurants)
		{
			if(restaurant.getRestaurantId().equals(accept.getRestId()))
			{
				for(Item item: restaurant.getItems())
				{
					if(item.getItemId().equals(accept.getItemId()) && item.getQuantity() >= accept.getQty())
					{
						log.info("Item before accept :" + item.getItemId()+" ||  "+item.getQuantity());
						item.setQuantity(item.getQuantity() - accept.getQty());
						sufficientQuantityFlag = true;
						log.info("Item after accept :" + item.getItemId()+" ||  "+item.getQuantity());
					}
				}
			}
		}
		
		return sufficientQuantityFlag;
	}
	
	
}
