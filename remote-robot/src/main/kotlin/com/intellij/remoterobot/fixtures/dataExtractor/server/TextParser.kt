// Copyright 2000-2020 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.

package com.intellij.remoterobot.fixtures.dataExtractor.server

import com.intellij.openapi.diagnostic.thisLogger
import com.intellij.remoterobot.data.TextData
import org.assertj.swing.edt.GuiActionRunner
import org.assertj.swing.edt.GuiTask
import java.awt.Component
import java.awt.Point
import java.awt.image.BufferedImage
import javax.swing.JViewport

object TextParser {
    private val graphics = BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB).createGraphics()
    private val logger = thisLogger()

    fun parseComponent(component: Component, textToKey: TextToKeyCache): List<TextData> {
        val containerComponent = findContainerComponent(component) ?: return emptyList()
        val x = containerComponent.locationOnScreen.x - component.locationOnScreen.x
        val y = containerComponent.locationOnScreen.y - component.locationOnScreen.y
        val data = mutableListOf<TextData>()

        val g = DataExtractorGraphics2d(graphics, data, Point(x, y), textToKey)
        parseData(g, containerComponent)
        return data.distinct()
    }

    fun parseCellRenderer(component: Component): List<String> {
        val data = mutableListOf<String>()

        val g = CellReaderGraphics2d(graphics, data)
        parseData(g, component)
        return data
    }

    private fun parseData(g: ExtractorGraphics2d, component: Component) {
        GuiActionRunner.execute(object : GuiTask() {
            override fun executeInEDT() {
                try {
                    component.paint(g)
                } catch (e: Exception) {
                    logger.error("Text parsing error. Can't do paint on ${component::class.java.simpleName}", e)
                }
            }
        })
    }

    private fun findContainerComponent(component: Component): Component? {
        if (component.parent is JViewport) {
            return component.parent
        }
        return component
    }
}