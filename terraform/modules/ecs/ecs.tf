# resource "aws_ecs_cluster" "user_main" {
#   name = "user-kickoff-cluster"
# }

resource "aws_ecs_cluster" "service_cluster" {
  for_each = var.services
  name     = each.value.cluster_name
}

# resource "aws_ecs_task_definition" "user_app" {
#   family                   = "user-app-task"
#   execution_role_arn       = aws_iam_role.ecs_tasks_role.arn
#   network_mode             = "awsvpc"
#   requires_compatibilities = ["FARGATE"]
#   cpu                      = var.fargate_cpu
#   memory                   = var.fargate_memory

#   # The container_definitions field is a JSON string that defines the container(s) to run in the task
#   container_definitions = templatefile("./template/ecs_json.tpl", {
#     app_name          = var.users_app_name
#     app_image         = var.users_app_image
#     app_port          = var.users_app_port
#     fargate_cpu       = var.fargate_cpu
#     fargate_memory    = var.fargate_memory
#     aws_region        = var.aws_region
#     log_group         = aws_cloudwatch_log_group.users_log_group.name
#     DATABASE_HOST     = var.kickoff_db_endpoint
#     DATABASE_PORT     = 3306
#     DATABASE_NAME     = var.database_name
#     DATABASE_USER     = var.database_username
#     DATABASE_PASSWORD = var.database_password
#   })
#   depends_on = [aws_iam_role_policy_attachment.task_execution_role_policy_attachment]
# }

resource "aws_ecs_task_definition" "app" {
  for_each = var.services

  family                   = "${each.key}-task"
  execution_role_arn       = aws_iam_role.ecs_tasks_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.fargate_cpu
  memory                   = var.fargate_memory

  container_definitions = templatefile("./template/ecs_json.tpl", {
    app_name          = each.key
    app_image         = each.value.app_image
    app_port          = each.value.app_port
    fargate_cpu       = var.fargate_cpu
    fargate_memory    = var.fargate_memory
    aws_region        = var.aws_region
    log_group         = "/ecs/${each.key}"
    DATABASE_HOST     = each.value.db_endpoint
    DATABASE_PORT     = 3306
    DATABASE_NAME     = var.database_name
    DATABASE_USER     = var.database_username
    DATABASE_PASSWORD = var.database_password
  })
}

# resource "aws_ecs_service" "user_main" {
#   name            = "users-service"
#   cluster         = aws_ecs_cluster.user_main.id
#   task_definition = aws_ecs_task_definition.user_app.arn
#   desired_count   = var.app_count
#   launch_type     = "FARGATE"

#   network_configuration {
#     security_groups  = var.ecs_tasks_sg_ids
#     subnets          = var.public_subnet_ids
#     assign_public_ip = true
#   }

#   load_balancer {
#     target_group_arn = aws_alb_target_group.users_app.id
#     container_name   = var.users_app_name
#     container_port   = var.users_app_port
#   }

#   depends_on = [aws_alb_listener.alb_main_listener, aws_iam_role_policy_attachment.task_execution_role_policy_attachment]
# }

resource "aws_ecs_service" "app" {
  for_each = var.services

  name            = "${each.key}-service"
  cluster         = aws_ecs_cluster.service_cluster[each.key].id # Associate the service with its cluster
  task_definition = aws_ecs_task_definition.app[each.key].arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = var.ecs_tasks_sg_ids
    subnets          = var.public_subnet_ids
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.app[each.key].id
    container_name   = each.key
    container_port   = each.value.app_port
  }

  depends_on = [aws_alb_listener.alb_main_listener, aws_iam_role_policy_attachment.task_execution_role_policy_attachment]
}