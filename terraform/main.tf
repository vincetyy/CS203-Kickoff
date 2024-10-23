module "network" {
  source = "./modules/network"
}

module "rds" {
  source               = "./modules/rds"
  security_group_id    = module.network.db_sg_id
  db_subnet_group_name = module.network.db_subnet_group_name

  # Database Connection Credentials
  database_name     = var.database_name
  database_username = var.DATABASE_USERNAME
  database_password = var.DATABASE_PASSWORD
}

module "ecs" {
  source              = "./modules/ecs"
  lb_sg_ids           = [module.network.lb_sg_id]
  vpc_id              = module.network.vpc_id
  ecs_tasks_sg_ids    = [module.network.ecs_tasks_sg_id]
  kickoff_db_endpoint = module.rds.kickoff_db_endpoint
  public_subnet_ids   = module.network.public_subnet_ids

  # Database Connection Credentials
  database_name     = var.database_name
  database_username = var.DATABASE_USERNAME
  database_password = var.DATABASE_PASSWORD

  services = {
    users = {
      cluster_name = "users-cluster"
      app_image    = "vincetyy/kickoff-users:latest"
      app_port     = 8081
      path_pattern = ["/api/v1/users*", "/api/v1/playerProfiles*"]
    }
    tournaments = {
      cluster_name = "tournaments-cluster"
      app_image    = "vincetyy/kickoff-tournaments:latest"
      app_port     = 8080
      path_pattern = ["/api/v1/tournaments*", "/api/v1/locations*"]
    }
    clubs = {
      cluster_name = "clubs-cluster"
      app_image    = "vincetyy/kickoff-clubs:latest"
      app_port     = 8082
      path_pattern = ["/api/v1/clubs*"]
    }
  }
}
