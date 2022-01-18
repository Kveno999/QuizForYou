package com.example.agro_town.models

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.webkit.MimeTypeMap
import com.example.agro_town.R

object Constans {
    const val USERS: String = "users"
    const val FIRST_NAME: String = "firstName"
    const val LAST_NAME: String = "lastName"
    const val AGROTOWNPREFERENCES: String = "AgroTownPrefs"
    const val LOGGED_IN_USERNAME: String = "logged_in_username"
    const val EXTRA_USER_DETAILS: String = "extra_user_details"
    const val READ_STORAGE_PERMISSION_CODE = 2
    const val PICK_IMAGE_REQUEST_CODE = 1
    const val MALE: String = "male"
    const val FEMALE: String = "female"
    const val MOBILE: String = "mobile"
    const val GENDER: String = "gender"
    const val USER_PROFILE_IMAGE: String = "User_Profile_Image"
    const val IMAGE: String = "image"
    const val COMPLETE_PROFILE: String = "profileCompleted"
//    const val USER_NAME: String = "user_name"
//    const val TOTAL_QUESTIONS: String = "total_questions"
//    const val CORRECT_ANSWERS: String = "correct_answers"



    fun showImageChooser(activity: Activity) {

        val galleryIntent = Intent(
            Intent.ACTION_PICK,
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        )

        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
    }


    fun getFileExtension(activity: Activity, uri: Uri?): String? {

        return  MimeTypeMap.getSingleton()
            .getExtensionFromMimeType(activity.contentResolver.getType(uri!!))

    }
    fun getQuestions(): ArrayList<Question> {
        val questionsList = ArrayList<Question>()

        // 1
        val que1 = Question(
            1, "მურმან დუმბაძის თანახმად, თუ \"ხუთიანს მოხაზავთ\", რა მოგივათ მარჯვენა ან მარცხენა ხელზე?",
            R.drawable.funny_question_1,
            "მნიშვნელობა არ აქვს რომელი ხელით მოხაზავ, ორივე შემთხვევაში დაკარგავ.", "თუ მარჯვენათი მოხაზავ მეჭეჭები ამოგივა,თუ მარცხენათი მუწუკები",
            "თუ მარჯვენა ხელით მოხაზავ კიბო გაგიჩნდება, თუ მარცხენათი - ინფექცია", "ყველანი მოვკვდებით", 3
        )

        questionsList.add(que1)

        // 2
        val que2 = Question(
            2, "რომელი არ არსებული პიროვნების გაღვიძებას ითხოვს მიხეილ ანდღულაძე?",
            R.drawable.bavshvi_gaagvidze,
            "სუპერ-მენის", "ჰალკის",
            "ადამიანი-ობობის", "ბავშვის", 4
        )

        questionsList.add(que2)

        // 3
        val que3 = Question(
            3, "რა ერქვა პროექტს, რომლის ფარგლებშიც ჭიათურის მოსახლეობას საარჩევნოდ ახალი კბილების ჩასმას დაპირდნენ?",
            R.drawable.gimiliani_saqartvelo,
            "კბილები ყველა ოჯახს", "ღიმილიანი საქართველო",
            "ჰოლივუდური ღიმილი", "გაიღიმე გიხდება", 2
        )

        questionsList.add(que3)

        // 4
        val que4 = Question(
            4, "ამ მოქალაქის განცხადებით, არჩევნებზე რამდენჯერ შემოხაზა?",
            R.drawable.samjer,
            "სამჯერ", "შვიდჯერ",
            "ორჯერ", "ნასვამი იყო, აღარ ახსოვს", 1
        )

        questionsList.add(que4)

        // 5
        val que5 = Question(
            5, "რომელი ასო დაასახელა იღბლიან ბორბალში ამ ახალგაზრდამ?",
            R.drawable.tansacmlis_kajada,
            "რ", "ლ",
            "მ", "ჟ", 4
        )

        questionsList.add(que5)

        // 6
        val que6 = Question(
            6, "რას პასუხობს ციცო აბუაშვილი ერთ-ერთ ამომრჩეველს ნათქვამზე: \"ბოდიშით, ნასვამი ვაარ\"?",
            R.drawable.waikvvanet_esa,
            "მერედა აქ რა გესაქმება, სახინკეა გახსნილი თუ რა?", "ვაიმე ეს ვის დაჰკარგვია!",
            "წაიყვანეთ ესა კაცო, წაიყვანეთ ესა, წაიყვანეთ!", "ცეცხლი და ნავთი დალიე შე არ გასახარებელო", 3
        )

        questionsList.add(que6)

        // 7
        val que7 = Question(
            7, "რამდენ ხინკალზე აქვს დადებული ნიძლავი დავით აქუბარდიას?",
            R.drawable.xuti_xinkali,
            "25", "5",
            "99", "1", 2
        )

        questionsList.add(que7)

        // 8
        val que8 = Question(
            8, "ამ ვარსკვლავად ქცეული დამკვირვებლის თქმით, როგორ მოექცნენ მას საარჩევნო უბანზე?",
            R.drawable.beiji_amaxia,
            "დამაცემა ასფალტზე", "ბეიჯი ამახია",
            "ყველა ჩამოთვლილი და კიდევ მეტიც", "მეჯანჯღრევოდა", 3
        )

        questionsList.add(que8)

        // 9
        val que9 = Question(
            9, "ამ კაცის თქმით, რას აკეთებს საარჩევნო უბანთან სიებით ხელში?",
            R.drawable.disertaciis_dacva,
            "დისერტაციას იცავს", "ლექსს წერს",
            "ეროვნულებში ვარჯიშობს", "კოტლინის კოდს წერს", 1
        )

        questionsList.add(que9)

        // 10
        val que10 = Question(
            10, "რომელ საყვარელ ადამიანს უწევს ხელს ამ მომენტში გრიშა ონიანი?",
            R.drawable.grisha,
            "მამას", "ძმას",
            "ცოლს", "ბადრი შუბლაძეს", 4
        )

        questionsList.add(que10)

        return questionsList
    }



}