
resource "aws_ecs_cluster" "user_main" {
  name = "user-kickoff-cluster"
}

resource "aws_ecs_task_definition" "user_app" {
  family                   = "user-app-task"
  execution_role_arn       = aws_iam_role.ecs_tasks_role.arn
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = var.fargate_cpu
  memory                   = var.fargate_memory

  # The container_definitions field is a JSON string that defines the container(s) to run in the task
  container_definitions = templatefile("./template/ecs_json.tpl", {
    app_image         = var.users_app_image
    app_port          = var.users_app_port
    fargate_cpu       = var.fargate_cpu
    fargate_memory    = var.fargate_memory
    aws_region        = var.aws_region
    log_group         = aws_cloudwatch_log_group.users_log_group.name
    DATABASE_HOST     = var.kickoff_db_endpoint
    DATABASE_PORT     = 3306
    DATABASE_NAME     = var.database_name
    DATABASE_USER     = var.database_username
    DATABASE_PASSWORD = var.database_password
  })
  depends_on = [aws_iam_role_policy_attachment.task_execution_role_policy_attachment]
}

resource "aws_ecs_service" "user_main" {
  name            = "users-service"
  cluster         = aws_ecs_cluster.user_main.id
  task_definition = aws_ecs_task_definition.user_app.arn
  desired_count   = var.app_count
  launch_type     = "FARGATE"

  network_configuration {
    security_groups  = var.ecs_tasks_sg_ids
    subnets          = var.public_subnet_ids
    assign_public_ip = true
  }

  load_balancer {
    target_group_arn = aws_alb_target_group.users_app.id
    container_name   = "" # Must match with the container name defined in template
    container_port   = var.users_app_port
  }

  depends_on = [aws_alb_listener.users_front_end, aws_iam_role_policy_attachment.task_execution_role_policy_attachment]
}