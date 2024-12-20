pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'develop', url: 'https://github.com/caadiq/seoulbike-server'
            }
        }

         stage('Copy .env') {
            steps {
                sh 'cp /var/jenkins_home/server/env/.env.seoulbike /var/jenkins_home/workspace/seoulbike/.env'
            }
        }

        stage('Build JAR') {
            steps {
                sh 'chmod +x ./gradlew'
                sh './gradlew clean build -x test'
            }
        }

//         stage('Deploy Container') {
//             steps {
//                 script {
//                     dir('/var/jenkins_home/workspace/seoulbike') {
//                         sh 'docker-compose down'
//                         sh 'docker-compose up -d'
//                     }
//                 }
//             }
//         }

        stage('Copy JAR') {
            steps {
                sh 'docker cp /var/jenkins_home/workspace/seoulbike/build/libs/seoulbike-1.0.0.jar seoulbike-springboot:/app/seoulbike.jar'
            }
        }

        stage('Restart Container') {
            steps {
                sh 'docker restart seoulbike-springboot'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        failure {
            echo 'Pipeline failed.'
        }
    }
}