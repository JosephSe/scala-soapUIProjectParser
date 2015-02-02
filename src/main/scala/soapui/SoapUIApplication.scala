package soapui

import java.io.File

import common.AppConstants
import model.{TestStep, TestSuit, TestCase}

import scala.xml.XML

object SoapUIReader {

  def readFile(filePath: String) = {
    val soapFile = XML.loadFile(filePath)
    val testSuits = (soapFile \\ "soapui-project" \\ "testSuite") map { testSuit =>
      val testSuitName = testSuit \ "@name" text
      val testCases = testSuit \\ "testCase" map { testCase =>
        val testCaseName = testCase \ "@name" text
        val testSteps: Seq[TestStep] = testCase \\ "testStep" map { testStep =>
          val stepName = (testStep \ "@name" text).toString.trim
          val disabled = (testStep \ "@disabled" text).toString.equals("true")
          val step = testStep \ "@type" toString match {
            case "httprequest" => new TestStep(stepName, "httprequest", Option(XML.loadString(testStep \\ "request" text)), disabled)
            case cnt => new TestStep(stepName, cnt)
          }
          step
        }
        TestCase(testCaseName.trim, testSteps)
      }
      TestSuit(testSuitName.trim, testCases)
    }
    testSuits
  }

  def printTestSuits(testSuits: Seq[TestSuit]) = {
    testSuits foreach { testSuit =>
      println(testSuit.name)
      testSuit.testCases foreach { testCase =>
        println(s"\t ${testCase.name}")
        testCase.testSteps foreach { testStep =>
          println(s"\t\t ${testStep.name}")
        }
      }
    }
  }
}

object SoapUIApplication extends App{


  val testSuits = SoapUIReader.readFile(AppConstants.soapUI_fileLocation)
  SoapUIReader.printTestSuits(testSuits)

}


