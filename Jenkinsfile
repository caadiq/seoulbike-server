pipeline {
    agent any

    stages {
        stage('Clone Repository') {
            steps {
                git branch: 'develop', url: 'https://github.com/caadiq/seoulbike-server'
            }
        }

//          stage('Copy .env') {
//             steps {
//                 sh 'cp /var/jenkins_home/server/env/.env.movie /var/jenkins_home/workspace/movie/.env'
//             }
//         }
//
//         stage('Build JAR') {
//             steps {
//                 sh 'chmod +x ./gradlew'
//                 sh './gradlew clean build -x test'
//             }
//         }

//         stage('Deploy Container') {
//             steps {
//                 script {
//                     dir('/var/jenkins_home/workspace/movie') {
//                         sh 'docker-compose down'
//                         sh 'docker-compose up -d'
//                     }
//                 }
//             }
//         }

//         stage('Copy JAR') {
//             steps {
//                 sh 'docker cp /var/jenkins_home/workspace/movie/build/libs/movie-1.0.0.jar movie-springboot:/app/movie.jar'
//             }
//         }
//
//         stage('Restart Container') {
//             steps {
//                 sh 'docker restart movie-springboot'
//             }
//         }
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