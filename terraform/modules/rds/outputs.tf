# This is the endpoint of the NestJS database instance
output "kickoff_db_endpoint" {
  value = aws_rds_cluster_instance.aurora_instance.endpoint
}
