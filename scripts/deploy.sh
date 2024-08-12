DEPLOYMENT_ACTIVE_PROFILES=$(echo ACTIVE_PROFILES)
SCRIPT_PATH="/home/ec2-user/action/scripts"

if [ -z "$DEPLOYMENT_ACTIVE_PROFILES" ]; then
  echo "Active profiles 가 설정되어 있지 않습니다."
  exit 1

elif [ "$DEPLOYMENT_ACTIVE_PROFILES" == "dev" ]; then
  chmod +x $SCRIPT_PATH/dev-deploy.sh
  $SCRIPT_PATH/dev-deploy.sh

elif [ "$DEPLOYMENT_ACTIVE_PROFILES" == "prod" ]; then
  chmod +x $SCRIPT_PATH/prod-deploy.sh
  $SCRIPT_PATH/prod-deploy.sh

else
  echo "Invalid Active profiles"
  exit 1
fi