# # Set up CloudWatch group and log stream and retain logs for 30 days
# resource "aws_cloudwatch_log_group" "users_log_group" {
#   name              = "/ecs/users-kickoff-app"
#   retention_in_days = 30

#   tags = {
#     Name = "users-log-group"
#   }
# }

# resource "aws_cloudwatch_log_stream" "users_log_stream" {
#   name           = "users-kickoff-log-stream"
#   log_group_name = aws_cloudwatch_log_group.users_log_group.name
# }

resource "aws_cloudwatch_log_group" "log_group" {
  for_each          = var.services
  name              = "/ecs/${each.key}"
  retention_in_days = 30

  tags = {
    Name = "${each.key}-log-group"
  }
}

resource "aws_cloudwatch_log_stream" "log_stream" {
  for_each       = var.services
  name           = "${each.key}-log-stream"
  log_group_name = aws_cloudwatch_log_group.log_group[each.key].name
}