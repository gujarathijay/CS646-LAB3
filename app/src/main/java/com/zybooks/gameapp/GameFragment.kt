package com.example.gameapp

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import com.zybooks.gameapp.R
import kotlin.random.Random

class GameFragment : Fragment() {
    private var randomNumber = 50
    private var maxGuesses = 5
    private var currentGuessCount = 0

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_game, container, false)

        // Retrieve the max guesses setting
        val preferences = PreferenceManager.getDefaultSharedPreferences(requireContext())
        maxGuesses = preferences.getString("max_guesses", "5")?.toIntOrNull() ?: 5


        val guessField = view.findViewById<EditText>(R.id.guessField)
        val guessButton = view.findViewById<Button>(R.id.guessButton)
        val resultText = view.findViewById<TextView>(R.id.resultText)

        guessButton.setOnClickListener {
            if (currentGuessCount < maxGuesses) {
                val userGuess = guessField.text.toString().toIntOrNull()
                if (userGuess != null) {
                    currentGuessCount++
                    if (userGuess > randomNumber) {
                        resultText.text = "Too high!"
                    } else if (userGuess < randomNumber) {
                        resultText.text = "Too low!"
                    } else {
                        resultText.text = "Correct! The number was $randomNumber."
                        showRestartDialog()
                    }
                    if (currentGuessCount == maxGuesses && userGuess != randomNumber) {
                        resultText.text = "You've reached the maximum guesses. The number was $randomNumber."
                        showMaxGuessDialog()
                    }
                } else {
                    resultText.text = "Please enter a number."
                }
            } else {
                resultText.text = "No more guesses allowed. Please restart the game."
            }
        }

        return view
    }

    private fun showRestartDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Congratulations!")
                .setMessage("You've guessed the right number. Do you want to play again?")
                .setPositiveButton("Yes") { dialog, which ->
                    restartGame()
                }
                .setNegativeButton("No") { dialog, which ->
                    activity?.finish() // Close the activity, can be replaced with any other action
                }
                .show()
        }
    }

    private fun showMaxGuessDialog() {
        activity?.let {
            AlertDialog.Builder(it)
                .setTitle("Opps!")
                .setMessage("You've reached the limit. Do you want to play again?")
                .setPositiveButton("Yes") { dialog, which ->
                    restartGame()
                }
                .setNegativeButton("No") { dialog, which ->
                    activity?.finish() // Close the activity, can be replaced with any other action
                }
                .show()
        }
    }

    private fun restartGame() {
        randomNumber = Random.nextInt(1, 100) // Reset the random number
        view?.findViewById<TextView>(R.id.resultText)?.text = "" // Clear the result text
        view?.findViewById<EditText>(R.id.guessField)?.text?.clear() // Clear the guess field
    }
}
