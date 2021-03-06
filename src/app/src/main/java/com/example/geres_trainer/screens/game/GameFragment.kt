package com.example.geres_trainer.screens.game


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.geres_trainer.R
import com.example.geres_trainer.database.translation.TranslationDB
import com.example.geres_trainer.databinding.GameFragmentBinding
import com.google.android.material.snackbar.Snackbar

class GameFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: GameFragmentBinding = DataBindingUtil.inflate(
            inflater, R.layout.game_fragment, container, false)


        val application = requireNotNull(this.activity).application

        val dataSource = TranslationDB.getInstance(application).translationDBDao

        val viewModelFactory = GameFragmentViewModelFactory(lifecycle, dataSource, application)

        val gameFragmentViewModel =
            ViewModelProviders.of(
                this, viewModelFactory).get(GameFragmentViewModel::class.java)

        var buttonClicked = false

        val arguments = GameFragmentArgs.fromBundle(arguments!!)

        binding.gameFragmentViewModel = gameFragmentViewModel
        gameFragmentViewModel.gameSize = arguments.gameLength


        /**
         * This Listener provides the logic when the ConfirmAnswerButton is clicked.
         */
        binding.comfirmAnswerButton.setOnClickListener {
            buttonClicked = true
            gameFragmentViewModel.onConfirmClick(binding.answerTextField.text.toString())
            binding.answerTextField.text.clear()
            binding.answerTextField.onEditorAction(EditorInfo.IME_ACTION_GO)
            buttonClicked = false
        }

        /**
         * This Listener is used to allow the user to confirm his answer with the enter button of his virtual keyboard.
         * It also closes the keyboard after pressing the enter button.
         */
        binding.answerTextField.setOnEditorActionListener {_, actionId, _ ->
            return@setOnEditorActionListener when (actionId) {
                EditorInfo.IME_ACTION_DONE -> {
                    if (!buttonClicked){
                        gameFragmentViewModel.onConfirmClick(binding.answerTextField.text.toString())
                        binding.answerTextField.text.clear()
                    }
                    false
                }
                else -> true
            }
        }


        /**
         * Observes the variable that tells the game if it is over and should go to the EndFragment
         */
        gameFragmentViewModel.gameIsDone.observe(this, Observer {
            this.findNavController().navigate(
                GameFragmentDirections
                    .actionGameFragmentToEndFragment(gameFragmentViewModel.wrongTranslations.toLongArray(),gameFragmentViewModel.points, gameFragmentViewModel.gameSize)
            )

        })

        /**
         * Observes the variable responsible for telling the game that the list is filled since the list filling is threaded.
         */
        gameFragmentViewModel.listIsFilled.observe(this, Observer {
            if(it && !gameFragmentViewModel.gameIsAlreadyRunning) {
                gameFragmentViewModel.startGame()
            }

        })


        gameFragmentViewModel.showSnackBarCorrect.observe(this, Observer {
            if (it == true) {
                val snackbar = Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.wordCorrectSnackBar_text),
                    Snackbar.LENGTH_SHORT)
                snackbar.view.setBackgroundColor(application.getColor(R.color.colorCorrectWord))
                snackbar.show()

                gameFragmentViewModel.doneShowCorrectSnackBar()

            }
        })

        gameFragmentViewModel.showSnackBarFalse.observe(this, Observer {
            if(it == true) {
                val snackbar = Snackbar.make(
                    activity!!.findViewById(android.R.id.content),
                    getString(R.string.wordFalseSnackBar_text) + "\t" + gameFragmentViewModel.answerWord,
                    Snackbar.LENGTH_LONG
                )
                snackbar.view.setBackgroundColor(application.getColor(R.color.colorFalseWord))
                snackbar.show()

                gameFragmentViewModel.doneShowFalseSnackBar()
            }
        })



        gameFragmentViewModel.initRandomGame()

        /**
         *  Used to start and stop the timer on specific lifecycle events
         */
        lifecycle.addObserver(gameFragmentViewModel)


        binding.setLifecycleOwner(this)






        return binding.root

    }





}
