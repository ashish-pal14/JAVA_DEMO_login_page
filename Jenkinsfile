pipeline {
    agent any

    stages {
        stage('clean') {
            steps {
               sh " mvn clean "
             }
        }
        stage('build') {
            steps {
                sh "mvn package"
            }
        }
        stage('deplloy') {
            steps {
                echo "it's depoloying"
                sh "pwd"

                sh 'nohup java -jar target/loginapp-0.0.1-SNAPSHOT.jar --server.port=8083 &'
            }
        }
    }
}