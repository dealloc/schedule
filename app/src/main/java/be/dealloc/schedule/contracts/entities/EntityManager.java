package be.dealloc.schedule.contracts.entities;
// Created by dealloc. All rights reserved.

public interface EntityManager<Entity>
{
	Entity create();

	Entity find(long id);

	void save(Entity entity);

	void delete(Entity entity);

	void delete(long id);
}
