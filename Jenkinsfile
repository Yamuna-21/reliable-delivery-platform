pipeline {
    agent any
    options {
        timestamps()
        disableConcurrentBuilds()
        buildDiscarder(logRotator(numToKeepStr: '10'))
    }
    environment {
        IMAGE_NAME = 'reliable-delivery-platform'
        IMAGE_TAG = "${BUILD_NUMBER}"
    }
    stages {
        stage('Checkout') { steps { checkout scm } }
        stage('Unit Tests') {
            steps { sh 'mvn -B clean test' }
            post { always { junit 'target/surefire-reports/*.xml' } }
        }
        stage('Package') { steps { sh 'mvn -B package -DskipTests' } }
        stage('Build Container') {
            steps { sh 'docker build -t ${IMAGE_NAME}:${IMAGE_TAG} -t ${IMAGE_NAME}:latest .' }
        }
        stage('Container Security Scan') {
            steps {
                sh '''
                  if ! command -v trivy >/dev/null 2>&1; then
                    echo "Trivy is required on the Jenkins agent. Install it before running this pipeline."
                    exit 1
                  fi
                  trivy image --exit-code 1 --severity CRITICAL,HIGH --ignore-unfixed ${IMAGE_NAME}:${IMAGE_TAG}
                '''
            }
        }
        stage('Deploy Locally') {
            when { expression { env.BRANCH_NAME == 'main' || env.GIT_BRANCH == 'origin/main' } }
            steps {
                sh '''
                  docker compose down || true
                  docker compose up -d --build
                '''
            }
        }
        stage('Smoke Test') {
            when { expression { env.BRANCH_NAME == 'main' || env.GIT_BRANCH == 'origin/main' } }
            steps {
                sh '''
                  for i in $(seq 1 20); do
                    if curl --fail --silent http://localhost:8080/actuator/health; then exit 0; fi
                    sleep 5
                  done
                  echo "Application did not become healthy in time"
                  docker compose logs app
                  exit 1
                '''
            }
        }
    }
    post {
        success { echo 'Pipeline completed: tests, package, image scan, deployment and smoke test passed.' }
        failure { echo 'Pipeline failed. Review the stage logs and fix the reported issue.' }
        always { echo "Build ${BUILD_NUMBER} finished with status: ${currentBuild.currentResult}" }
    }
}
