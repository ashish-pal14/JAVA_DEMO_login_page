pipeline {
    agent any

    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('test') {
            steps {
                sh  'mvn test'
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
                    
                    export JENKINS_NODE_COOKIE=dshbfiujhdf
                    nohup java -jar target/loginapp-0.0.1-SNAPSHOT.jar \
                        --server.port=8083 \
                        > logs.cat 2>&1 &

                   
                '''
            }
        }
    }
}