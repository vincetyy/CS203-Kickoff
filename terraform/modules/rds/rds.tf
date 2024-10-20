resource "aws_rds_cluster" "aurora_cluster" {
  cluster_identifier     = "kickoff-cluster"
  engine                 = "aurora-mysql"
  engine_mode            = "provisioned"             # only v1 is serverless, serverless_v2 must be `provisioned`, v2 is not true serverless 
  engine_version         = "8.0.mysql_aurora.3.05.2" # compatible with mysql8.0
  db_subnet_group_name   = var.db_subnet_group_name
  database_name          = var.database_name
  master_username        = var.database_username
  master_password        = var.database_password
  storage_encrypted      = false # set this to false for aurora serverless, default true
  skip_final_snapshot    = true
  vpc_security_group_ids = [var.security_group_id]

  serverlessv2_scaling_configuration {
    max_capacity = 1.0
    min_capacity = 0.5
  }
}

resource "aws_rds_cluster_instance" "aurora_instance" {
  identifier          = "kickoff-instance"
  cluster_identifier  = aws_rds_cluster.aurora_cluster.id
  instance_class      = "db.serverless"
  engine              = aws_rds_cluster.aurora_cluster.engine
  engine_version      = aws_rds_cluster.aurora_cluster.engine_version
  publicly_accessible = true
}
