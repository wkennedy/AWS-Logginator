package com.github.wkennedy.services

trait CredentialTrait {

  def AWS_ACCESS_KEY = System.getProperty("awsAccessKey")
  def AWS_SECRET_KEY = System.getProperty("awsSecretKey")

}
