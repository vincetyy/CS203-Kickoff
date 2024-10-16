module "network" {
  source                 = "./modules/network"
}

module "rds" {
  source                = "./modules/rds"
  security_group_id     = module.network.db_sg_id
  db_subnet_group_name  = module.network.db_subnet_group_name

  # Database Connection Credentials
  database_name     = var.database_name
  database_username = var.DATABASE_USERNAME
  database_password = var.DATABASE_PASSWORD
}

module "ecs" {
  source                 = "./modules/ecs"
  lb_sg_ids              = [module.network.lb_sg_id]
  vpc_id                 = module.network.vpc_id
  ecs_tasks_sg_ids       = [module.network.ecs_tasks_sg_id]
  kickoff_db_endpoint    = module.rds.kickoff_db_endpoint
  public_subnet_ids      = module.network.public_subnet_ids

  # Database Connection Credentials
  database_name     = var.database_name
  database_username = var.DATABASE_USERNAME
  database_password = var.DATABASE_PASSWORD
}
