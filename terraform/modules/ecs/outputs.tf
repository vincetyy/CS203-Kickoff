output "users_alb_hostname" {
  value = "${aws_alb.users_main.dns_name}:3000"
}