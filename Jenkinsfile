pipeline{
    agent {
            docker {
                image 'maven:3-alpine'
                args '-v /root/.m2:/root/.m2'
            }
        }

    stages{
        stage('Print Message'){
            steps{
                echo 'Printing message'
            }

        }

        stage('Delete Workspace'){
            steps{
                echo 'CLEAR WORKSPACE: ${WORKSPACE}'
            }
        }

        stage('Build'){
            steps{
                sh "mvn -Dmaven.test.failure.ignore=true clean package"
            }

        }
    }


}