pipeline{
    agent any

    stages{
        stage('Print Message'){
            steps{
                echo 'Printing message'
                sh "docker -v"
                sh "docker-compose -v"
            }

        }

        stage('Delete Workspace'){
            steps{
                echo 'CLEAR WORKSPACE: ${WORKSPACE}'
            }
        }

        stage('Build'){
            steps{
                echo "start building project"
                sh "mvn -DskipTests clean package"
            }
        }

        stage("Deploy"){
            steps{
                echo "start deploying to docker"
            }
        }
    }


}