pipeline {
    agent any

    stages {
        stage('clean') {
            steps {
                sh 'mvn clean'
            }
        }

        stage('test') {
            parallel {
                stage('unit-tests') {
                    steps {
                        sh 'mvn -Dtest=LoginServiceTest,ShopServiceTest test'
                    }
                }
                stage('web-tests') {
                    steps {
                        sh 'mvn -Dtest=AuthControllerTest,ShopControllerTest test'
                    }
                }
                stage('integration-tests') {
                    steps {
                        sh 'mvn -Dtest=*IntegrationTest test'
                    }
                }
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
                    
                    PID=$(lsof -ti:8083)
                    
                    kill -15 "$PID"
                    
                    export JENKINS_NODE_COOKIE=dshbfiujhdf
                    
                    nohup java -jar target/loginapp-0.0.1-SNAPSHOT.jar \
                        --server.port=8083 \
                        > logs.cat 2>&1 &

                   
                '''
            }
        }
    }
}