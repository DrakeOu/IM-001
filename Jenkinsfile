pipeline{
    agent any
    stages{
        stage('Print Message'){
            steps{
                echo 'Printing message'
                echo "Project_Pipeline_name: ${JOB_NAME}"
                echo "Project_module_name: ${PROJECT_NAME}"
                echo "workspace: ${WORKSPACE}"
                echo "branch: ${Branch_name}"           //gitlab分支名
                echo "build_id: ${BUILD_ID}"
                echo "target_action: ${action}"
                echo "registryUrl: ${registryUrl}"
                echo "image_repository: ${registryUrl}/${Project_name}"
            }

        }

        stage('Delete Workspace'){
            steps{
                echo 'CLEAR WORKSPACE: ${WORKSPACE}'
            }
        }


    }


}