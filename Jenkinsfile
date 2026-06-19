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
            }
        }
    }
}