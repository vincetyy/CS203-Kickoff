resource "aws_subnet" "public_subnet_1" {
  vpc_id                  = aws_vpc.vpc.id
  availability_zone       = "ap-southeast-1a"
  cidr_block              = cidrsubnet(aws_vpc.vpc.cidr_block, 4, 1)
  map_public_ip_on_launch = true

  tags = {
    Name = "kickoff-public-subnet-1"
  }
}

resource "aws_subnet" "public_subnet_2" {
  vpc_id                  = aws_vpc.vpc.id
  availability_zone       = "ap-southeast-1b"
  cidr_block              = cidrsubnet(aws_vpc.vpc.cidr_block, 4, 2)
  map_public_ip_on_launch = true

  tags = {
    Name = "kickoff-public-subnet-2"
  }
}

resource "aws_subnet" "private_subnet_1" {
  vpc_id                  = aws_vpc.vpc.id
  availability_zone       = "ap-southeast-1a"
  cidr_block              = cidrsubnet(aws_vpc.vpc.cidr_block, 4, 3)
  map_public_ip_on_launch = false

  tags = {
    Name = "kickoff-private-subnet-1"
  }
}

resource "aws_subnet" "private_subnet_2" {
  vpc_id                  = aws_vpc.vpc.id
  availability_zone       = "ap-southeast-1b"
  cidr_block              = cidrsubnet(aws_vpc.vpc.cidr_block, 4, 4)
  map_public_ip_on_launch = false

  tags = {
    Name = "kickoff-private-subnet-2"
  }
}

resource "aws_db_subnet_group" "db_subnet_group" {
  name = "kickoff-db-subnet-group"
  subnet_ids = [
    aws_subnet.private_subnet_1.id,
    aws_subnet.private_subnet_2.id
  ]

  tags = {
    Name = "kickoff-db-subnet-group"
  }
}

resource "aws_db_subnet_group" "db_public_subnet_group" {
  name = "kickoff-db-public-subnet-group"
  subnet_ids = [
    aws_subnet.public_subnet_1.id,
    aws_subnet.public_subnet_2.id
  ]

  tags = {
    Name = "kickoff-db-public-subnet-group"
  }
}


resource "aws_route_table_association" "public_subnet_1" {
  subnet_id      = aws_subnet.public_subnet_1.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "public_subnet_2" {
  subnet_id      = aws_subnet.public_subnet_2.id
  route_table_id = aws_route_table.public_route_table.id
}

resource "aws_route_table_association" "private_subnet_association_1" {
  subnet_id      = aws_subnet.private_subnet_1.id
  route_table_id = aws_route_table.private_route_table.id
}

resource "aws_route_table_association" "private_subnet_association_2" {
  subnet_id      = aws_subnet.private_subnet_2.id
  route_table_id = aws_route_table.private_route_table.id
}