#!/bin/bash

set -xu

echo “creating admin user”
curl -v http://localhost:8000/users/register -d @data/create_admin_user.json --header "Content-Type: application/json"
echo “creating admin user”
curl -v http://localhost:8000/users/register -d @data/create_user.json --header "Content-Type: application/json"
echo “reciever call”
curl -v http://localhost:8000/callreceiver/add/reciever -d @data/create_call_receiver.json --header "Content- Type: application/json"
echo “make call”
curl -v http://localhost:8000/calls/make/call -d data/create_call.json --header "Content- Type: application/json"
