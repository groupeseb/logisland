#!groovy

node {

    /* PARAMS :
        DEPLOY_PROFILE : full / config
    */

    def mvnHome

    def git_repository_url = 'https://github.com/groupeseb/logisland.git'
    def git_credential_id = 'c883a18f-3c5d-4015-9ce3-524b2f45d824'
    def ansible_credentials_id = '6d61644b-e985-45b3-8344-6c44af8143d6'
    def ansible_inventory = '${WORKSPACE_ANSIBLE}/inventory-${ENVIRONNEMENT}'
    def ansible_playbook = '${WORKSPACE_ANSIBLE}/playbook-deploy-logisland.yml'
    def docker_image = 'groupeseb/logisland-hdp2.5'
    def env_proxy = '${ENV_PROXY}'

    stage('Show variables') {
        echo sh(script: 'env|sort', returnStdout: true)
    }

    stage('Download sources') {
        checkout scm: [
            $class: 'GitSCM',
            userRemoteConfigs: [[
                url: git_repository_url,
                credentialsId: git_credential_id]],
            branches: [[name: BRANCH_NAME]]],
            changelog: false, poll: false
    }

    stage('Build LogIsland SEB config') {
        if (params.DEPLOY_PROFILE == 'config') {
            mvnHome = tool 'mvn'
            sh "'${mvnHome}/bin/mvn' -f ${WORKSPACE}/logisland-framework/logisland-resources/pom.xml clean install -Dhdp=2.5 -DskipTests"
        }
    }

    stage('Build LogIsland & Docker image') {
        if (params.DEPLOY_PROFILE == 'full') {
            mvnHome = tool 'mvn'
            sh "'${mvnHome}/bin/mvn' -f ${WORKSPACE}/pom.xml clean install -Dhdp=2.5 -Pdocker -DskipTests -Dproxy=${env_proxy}"
        }
    }

    stage('Delete old package') {
        sh "rm -rf ${WORKSPACE}/package"
    }

    stage('Package LogIsland') {
        sh "mkdir -p ${WORKSPACE}/package/logisland/conf"
        sh "mkdir -p ${WORKSPACE}/package/logisland/docker"
        sh "unzip -p ${WORKSPACE}/logisland-framework/logisland-resources/target/logisland-resources-*.zip conf/seb-logisland.j2.yml > ${WORKSPACE}/package/logisland/conf/seb-logisland.j2.yml"
        if (params.DEPLOY_PROFILE == 'full') {
            sh "docker save -o ${WORKSPACE}/package/logisland/docker/logisland-${BUILD_NUMBER}.docker ${docker_image}"
        }
    }

    stage('Deploy LogIsland ') {
        if (params.DEPLOY_PROFILE == 'config') {
            ansiblePlaybook(
                credentialsId: '6d61644b-e985-45b3-8344-6c44af8143d6',
                installation: 'ansible',
                inventory: '${WORKSPACE_ANSIBLE}/inventory-${ENV}',
                playbook: '${WORKSPACE_ANSIBLE}/playbook-deploy-logisland.yml',
                extras: '--extra-vars "WORKSPACE=${WORKSPACE} JENKINS_URL=${JENKINS_URL} JOB_NAME=${JOB_NAME} BUILD_URL=${BUILD_URL} BUILD_ID=${BUILD_ID} BUILD_NUMBER=${BUILD_NUMBER} ',
                tags: 'logisland-config'
            )
        } else {
            ansiblePlaybook(
                credentialsId: '6d61644b-e985-45b3-8344-6c44af8143d6',
                installation: 'ansible',
                inventory: '${WORKSPACE_ANSIBLE}/inventory-${ENV}',
                playbook: '${WORKSPACE_ANSIBLE}/playbook-deploy-logisland.yml',
                extras: '--extra-vars "WORKSPACE=${WORKSPACE} JENKINS_URL=${JENKINS_URL} JOB_NAME=${JOB_NAME} BUILD_URL=${BUILD_URL} BUILD_ID=${BUILD_ID} BUILD_NUMBER=${BUILD_NUMBER} '
            )
        }
    }

}