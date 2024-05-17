terraform {
  required_providers {
    mysql = {
      source = "terraform-providers/mysql"
    }
  }
}

resource "random_password" "db_pass" {
  length = 32
  special = true
  override_special = "*_#%@"
}

variable "database_ip" {
  type = string
  default = "sql3.minestrator.com"
}

variable "database_user" {
  type = string
  default = "minesr_bw2LdGg4"
}

variable "database_password" {
  type = string
  default = "bNt4pqLQjjBlgMc2"
}

variable "database_name" {
  type = string
  default = "minesr_bw2LdGg4"
}


provider "mysql" {
  endpoint = var.database_ip
  username = var.database_user
  password = var.database_password
}

resource "null_resource" "run_sql" {
  triggers = {
    always_run = "${timestamp()}"
  }

  provisioner "local-exec" {
    command = "mysql --host=${var.database_ip} --user=${var.database_user} --password=${var.database_password} ${var.database_name} < sql/init_bdd.sql"
  }
}