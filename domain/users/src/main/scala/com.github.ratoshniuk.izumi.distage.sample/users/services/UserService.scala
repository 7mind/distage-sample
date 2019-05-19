package com.github.ratoshniuk.izumi.distage.sample.users.services

import com.github.pshirshov.izumi.functional.bio.BIO
import com.github.pshirshov.izumi.functional.bio.BIO._
import com.github.ratoshniuk.izumi.distage.sample.Models.CommonFailure
import com.github.ratoshniuk.izumi.distage.sample.users.services.models.{Email, User}

trait UserService[F[+_, +_]] {
  def upsert(userId: Int, email: Email): F[CommonFailure, Unit]
  def retrieve(email: Email): F[CommonFailure, User]
  def delete(email: Email): F[CommonFailure, Unit]
}

object UserService {

  final class Impl[F[+ _, + _] : BIO]
  (
    storage: UserPersistence[F]
  , externalStorage: UserThirdParty[F]
  ) extends UserService[F] {

    def upsert(userId: Int, email: Email): F[CommonFailure, Unit] = {
      for {
        data <- externalStorage.fetchUser(userId)
        res <- storage.upsert(data.toUser(email))
      } yield res
    }

    def retrieve(email: Email): F[CommonFailure, User] = {
      storage.get(email)
    }

    def delete(email: Email): F[CommonFailure, Unit] = {
      storage.remove(email)
    }
  }
}

