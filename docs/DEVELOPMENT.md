# Development

## Minikube

```shell
minikube config set driver hyperkit
minikube start --addons ingress,dashboard
minikube dashboard
minikube tunnel
echo "$(minikube ip) minikube.info web-simple-dev-websitecd-examples.minikube.info web-simple-prod-websitecd-examples.minikube.info web-advanced-dev-websitecd-examples.minikube.info web-advanced-prod-websitecd-examples.minikube.info" | sudo tee -a /etc/hosts

kubectl create namespace websitecd-examples
```

## Local Development

### Build project

```shell
mvn clean install
```

### Run Operator Locally
Default values for dev mode are stored in [application.properties](../service/src/main/resources/application.properties)
in section `# DEV`

```shell
cd service
mvn quarkus:dev
```

## Webhook API Development

Init websites git repos

```shell
rm -rf /tmp/repos; mkdir /tmp/repos
cp config/src/test/resources/gitconfig-test.yaml /tmp/repos/static-content-config.yaml
docker run --rm -e "CONFIG_PATH=/app/data/static-content-config.yaml" -e "TARGET_DIR=/app/data" -e "GIT_SSL_NO_VERIFY=true" -v "/tmp/repos/:/app/data/" quay.io/websitecd/content-git-init
```

Start content-git-api on port 8090

```shell
docker run --rm -e "APP_DATA_DIR=/app/data" -v "/tmp/repos/:/app/data/" -p 8090:8090 quay.io/websitecd/content-git-api
```

Fire event:

```shell
WEBHOOK_URL=http://localhost:8080/api/webhook
WEBHOOK_URL=https://operator-websitecd-examples.int.open.paas.redhat.com/api/webhook
curl -i -X POST $WEBHOOK_URL  -H "Content-Type: application/json" -H "X-Gitlab-Event: Push Hook" -H "X-Gitlab-Token: CHANGEIT" --data-binary "@src/test/resources/gitlab-push.json" 
curl -i -X POST $WEBHOOK_URL  -H "Content-Type: application/json" -H "X-Gitlab-Event: Push Hook" -H "X-Gitlab-Token: CHANGEIT" --data-binary "@src/test/resources/gitlab-push-website-changed.json" 
```


## Build Docker Image

```shell
mvn clean install
cd service
docker build -f src/main/docker/Dockerfile.jvm -t websitecd/operator-jvm .
docker tag websitecd/operator-jvm quay.io/websitecd/operator-jvm
docker push quay.io/websitecd/operator-jvm

# Restart running operator to pick newest version
kubectl rollout restart deployment websitecd-operator
```