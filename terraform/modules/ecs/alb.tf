resource "aws_alb" "users_main" {
  name            = "users-kickoff-lb"
  subnets         = var.public_subnet_ids
  security_groups = var.lb_sg_ids
}

resource "aws_alb_target_group" "users_app" {
  name        = "users-kickoff-target-group"
  port        = 80
  protocol    = "HTTP"
  vpc_id      = var.vpc_id
  target_type = "ip"

  health_check {
    healthy_threshold   = "2"
    interval            = "30"
    protocol            = "HTTP"
    matcher             = "200"
    timeout             = "3"
    path                = var.health_check_path
    unhealthy_threshold = "2"
  }
}

# Redirect all traffic from the ALB to the target group
resource "aws_alb_listener" "users_front_end" {
  load_balancer_arn = aws_alb.users_main.id
  port              = 80
  protocol          = "HTTP"

  default_action {
    target_group_arn = aws_alb_target_group.users_app.id
    type             = "forward"
  }
}