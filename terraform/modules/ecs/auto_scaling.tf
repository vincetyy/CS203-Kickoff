# resource "aws_appautoscaling_target" "users_target" {
#   service_namespace  = "ecs"
#   resource_id        = "service/${aws_ecs_cluster.user_main.name}/${aws_ecs_service.user_main.name}"
#   scalable_dimension = "ecs:service:DesiredCount"
#   role_arn           = aws_iam_role.ecs_autoscaling_role.arn
#   min_capacity       = 2
#   max_capacity       = 3
#   depends_on         = [aws_iam_role_policy_attachment.ecs_autoscaling_policy_attachment]
# }

resource "aws_appautoscaling_target" "app" {
  for_each           = var.services
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.service_cluster[each.key].name}/${aws_ecs_service.app[each.key].name}"
  scalable_dimension = "ecs:service:DesiredCount"
  role_arn           = aws_iam_role.ecs_autoscaling_role.arn
  min_capacity       = 1
  max_capacity       = 1

  depends_on = [aws_iam_role_policy_attachment.ecs_autoscaling_policy_attachment]
}


# # Automatically scale capacity up by one
# resource "aws_appautoscaling_policy" "user_up" {
#   name               = "scale_up"
#   service_namespace  = "ecs"
#   resource_id        = "service/${aws_ecs_cluster.user_main.name}/${aws_ecs_service.user_main.name}"
#   scalable_dimension = "ecs:service:DesiredCount"

#   step_scaling_policy_configuration {
#     adjustment_type         = "ChangeInCapacity"
#     cooldown                = 60
#     metric_aggregation_type = "Maximum"

#     step_adjustment {
#       metric_interval_lower_bound = 0
#       scaling_adjustment          = 1
#     }
#   }

#   depends_on = [aws_appautoscaling_target.users_target]
# }

# Scale Up Policy
resource "aws_appautoscaling_policy" "scale_up" {
  for_each = var.services

  name               = "${each.key}-scale-up"
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.service_cluster[each.key].name}/${aws_ecs_service.app[each.key].name}"
  scalable_dimension = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = 1
    }
  }

  depends_on = [aws_appautoscaling_target.app, ]
}

# # Automatically scale capacity down by one
# resource "aws_appautoscaling_policy" "user_down" {
#   name               = "scale_down"
#   service_namespace  = "ecs"
#   resource_id        = "service/${aws_ecs_cluster.user_main.name}/${aws_ecs_service.user_main.name}"
#   scalable_dimension = "ecs:service:DesiredCount"

#   step_scaling_policy_configuration {
#     adjustment_type         = "ChangeInCapacity"
#     cooldown                = 60
#     metric_aggregation_type = "Maximum"

#     step_adjustment {
#       metric_interval_lower_bound = 0
#       scaling_adjustment          = -1
#     }
#   }

#   depends_on = [aws_appautoscaling_target.users_target]
# }

# Automatically scale capacity down by one
resource "aws_appautoscaling_policy" "scale_down" {
  for_each = var.services

  name               = "${each.key}-scale-down"
  service_namespace  = "ecs"
  resource_id        = "service/${aws_ecs_cluster.service_cluster[each.key].name}/${aws_ecs_service.app[each.key].name}"
  scalable_dimension = "ecs:service:DesiredCount"

  step_scaling_policy_configuration {
    adjustment_type         = "ChangeInCapacity"
    cooldown                = 60
    metric_aggregation_type = "Maximum"

    step_adjustment {
      metric_interval_lower_bound = 0
      scaling_adjustment          = -1
    }
  }

  depends_on = [aws_appautoscaling_target.app]
}

# # CloudWatch alarm that triggers the autoscaling up policy
# resource "aws_cloudwatch_metric_alarm" "user_service_cpu_high" {
#   alarm_name          = "cpu_utilization_high"
#   comparison_operator = "GreaterThanOrEqualToThreshold"
#   evaluation_periods  = "2"
#   metric_name         = "CPUUtilization"
#   namespace           = "AWS/ECS"
#   period              = "60"
#   statistic           = "Average"
#   threshold           = "85"

#   dimensions = {
#     ClusterName = aws_ecs_cluster.user_main.name
#     ServiceName = aws_ecs_service.user_main.name
#   }

#   alarm_actions = [aws_appautoscaling_policy.user_up.arn]
# }

# CloudWatch Alarm for scaling up (high CPU)
resource "aws_cloudwatch_metric_alarm" "cpu_high" {
  for_each            = var.services
  alarm_name          = "${each.key}-cpu-high"
  comparison_operator = "GreaterThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = 85

  dimensions = {
    ClusterName = each.value.cluster_name
    ServiceName = "${each.key}-service"
  }

  alarm_actions = [aws_appautoscaling_policy.scale_up[each.key].arn]
}

# # CloudWatch alarm that triggers the autoscaling down policy
# resource "aws_cloudwatch_metric_alarm" "users_service_cpu_low" {
#   alarm_name          = "cpu_utilization_low"
#   comparison_operator = "LessThanOrEqualToThreshold"
#   evaluation_periods  = "2"
#   metric_name         = "CPUUtilization"
#   namespace           = "AWS/ECS"
#   period              = "60"
#   statistic           = "Average"
#   threshold           = "10"

#   dimensions = {
#     ClusterName = aws_ecs_cluster.user_main.name
#     ServiceName = aws_ecs_service.user_main.name
#   }

#   alarm_actions = [aws_appautoscaling_policy.user_down.arn]
# }

# CloudWatch Alarm for scaling down (low CPU)
resource "aws_cloudwatch_metric_alarm" "cpu_low" {
  for_each            = var.services
  alarm_name          = "${each.key}-cpu-low"
  comparison_operator = "LessThanOrEqualToThreshold"
  evaluation_periods  = "2"
  metric_name         = "CPUUtilization"
  namespace           = "AWS/ECS"
  period              = "60"
  statistic           = "Average"
  threshold           = 85

  dimensions = {
    ClusterName = each.value.cluster_name
    ServiceName = "${each.key}-service"
  }

  alarm_actions = [aws_appautoscaling_policy.scale_down[each.key].arn]
}