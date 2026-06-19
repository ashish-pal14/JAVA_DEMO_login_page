pipeline {
    agent any

    stages {
        stage('build') {
            steps {
               sh " mvn clean "
             }
        }
        stage('test') {
            steps {
                echo "It's testing"
            }
        }
        stage('deplloy') {
            steps {
                echo "it's depoloying"
            }
        }
    }
}