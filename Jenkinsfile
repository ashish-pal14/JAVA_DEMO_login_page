pipeline {
    agent any

    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('build') {
            steps {
                sh 'mvn package'
            }
        }

        stage('deploy') {
            steps {
                sh '''
                    

                    nohup java -jar target/loginapp-0.0.1-SNAPSHOT.jar \
                        --server.port=8083 \
                        > logs.cat 2>&1 &

                   
                '''
            }
        }
    }
}