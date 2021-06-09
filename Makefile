infra-start:
	docker compose up tenantdb

infra-stop:
	docker compose stop tenantdb

infra-stop-clean:
	docker compose down -v tenantdb
