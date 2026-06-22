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
                    export JENKINS_NODE_COOKIE=dontKillMe

                    nohup java -jar target/loginapp-0.0.1-SNAPSHOT.jar \
                        --server.port=8083 \
                        > logs.cat 2>&1 &

                    echo $! > app.pid

                    sleep 10

                    cat logs.cat || true

                    ps -fp $(cat app.pid) || true

                    ss -tulnp | grep 8083 || true
                '''
            }
        }
    }
}