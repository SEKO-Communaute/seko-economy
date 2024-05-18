#!/bin/bash

database_ip="sql3.minestrator.com"
database_user="minesr_bw2LdGg4"
database_password="bNt4pqLQjjBlgMc2"
database_name="minesr_bw2LdGg4"

mysql --host=${database_ip} --user=${database_user} --password=${database_password} ${database_name} < sql/destroy_bdd.sql